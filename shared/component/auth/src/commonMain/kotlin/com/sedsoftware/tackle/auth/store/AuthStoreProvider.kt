package com.sedsoftware.tackle.auth.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.auth.domain.AuthFlowManager
import com.sedsoftware.tackle.auth.model.CredentialsState
import com.sedsoftware.tackle.auth.model.InstanceInfoState
import com.sedsoftware.tackle.auth.model.ObtainedCredentials
import com.sedsoftware.tackle.auth.store.AuthStore.Intent
import com.sedsoftware.tackle.auth.store.AuthStore.Label
import com.sedsoftware.tackle.auth.store.AuthStore.State
import com.sedsoftware.tackle.domain.StoreCreate
import com.sedsoftware.tackle.domain.TackleException.MissedRegistrationData
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.utils.extension.isUnauthorized
import com.sedsoftware.tackle.utils.extension.isValidUrl
import com.sedsoftware.tackle.utils.extension.toNormalizedUrl
import com.sedsoftware.tackle.utils.extension.trimUrl
import com.sedsoftware.tackle.utils.extension.unwrap
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class AuthStoreProvider(
    private val storeFactory: StoreFactory,
    private val manager: AuthFlowManager,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
) {

    @StoreCreate
    fun create(autoInit: Boolean = true): AuthStore =
        object : AuthStore, Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
            name = "AuthStore",
            initialState = State(),
            autoInit = autoInit,
            bootstrapper = coroutineBootstrapper(mainContext) {
                dispatch(Action.CheckCurrentCredentials)
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
                var job: Job? = null

                onAction<Action.CheckCurrentCredentials> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.verifyCredentials() },
                            onSuccess = { isCredentialsValid: Boolean ->
                                if (isCredentialsValid) {
                                    dispatch(Msg.CredentialsStateChanged(newState = CredentialsState.AUTHORIZED))
                                    publish(Label.NavigateToMainScreen)
                                } else {
                                    dispatch(Msg.CredentialsStateChanged(newState = CredentialsState.UNAUTHORIZED))
                                }
                            },
                            onError = { throwable: Throwable ->
                                if (throwable is MissedRegistrationData || throwable.isUnauthorized) {
                                    dispatch(Msg.CredentialsStateChanged(newState = CredentialsState.UNAUTHORIZED))
                                } else {
                                    dispatch(Msg.CredentialsStateChanged(newState = CredentialsState.EXISTING_USER_CHECK_FAILED))
                                }
                                publish(Label.ErrorCaught(throwable))
                            },
                        )
                    }
                }

                onAction<Action.StartOAuthFlow> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.startAuthFlow(it.credentials) },
                            onSuccess = { isAuthorized: Boolean ->
                                if (isAuthorized) {
                                    forward(Action.CheckCurrentCredentials)
                                } else {
                                    dispatch(Msg.CredentialsStateChanged(newState = CredentialsState.UNAUTHORIZED))
                                }
                            },
                            onError = { throwable: Throwable ->
                                dispatch(Msg.OAuthFlowStateChanged(active = false))
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onIntent<Intent.OnTextInput> {
                    dispatch(Msg.TextInput(text = it.text))
                    job?.cancel()
                    job = launch {
                        delay(manager.getTextInputEndDelay())

                        val state = state()
                        val url = state.userInput
                        val displayedUrl = url.trimUrl()

                        dispatch(Msg.TextInput(text = displayedUrl))

                        if (displayedUrl.isValidUrl()) {
                            dispatch(Msg.ServerInfoLoadingStarted)

                            val normalizedUrl = url.toNormalizedUrl()

                            unwrap(
                                result = withContext(ioContext) { manager.getInstanceInfo(normalizedUrl) },
                                onSuccess = { info: Instance ->
                                    dispatch(Msg.ServerInfoLoaded(info = info))
                                },
                                onError = { throwable: Throwable ->
                                    dispatch(Msg.ServerInfoLoadingFailed)
                                    publish(Label.ErrorCaught(throwable))
                                },
                            )
                        }
                    }
                }

                onIntent<Intent.OnRetryButtonClicked> {
                    dispatch(Msg.CredentialsStateChanged(newState = CredentialsState.RETRYING))
                    forward(Action.CheckCurrentCredentials)
                }

                onIntent<Intent.OnAuthenticateButtonClicked> {
                    dispatch(Msg.OAuthFlowStateChanged(active = true))
                    val domain = state().instanceInfo.domain

                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.createApp(domain) },
                            onSuccess = { credentials: ObtainedCredentials ->
                                forward(Action.StartOAuthFlow(credentials))
                            },
                            onError = { throwable: Throwable ->
                                dispatch(Msg.OAuthFlowStateChanged(active = false))
                                publish(Label.ErrorCaught(throwable))
                            },
                        )
                    }
                }

                onIntent<Intent.ShowLearnMore> { dispatch(Msg.LearnMoreVisibilityChanged(visible = it.show)) }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.TextInput -> copy(
                        userInput = msg.text,
                        instanceInfoState = InstanceInfoState.IDLE,
                    )

                    is Msg.ServerInfoLoadingStarted -> copy(
                        instanceInfoState = InstanceInfoState.LOADING,
                    )

                    is Msg.ServerInfoLoaded -> copy(
                        instanceInfo = msg.info,
                        instanceInfoState = if (msg.info.title.isNotEmpty()) {
                            InstanceInfoState.LOADED
                        } else {
                            InstanceInfoState.ERROR
                        },
                    )

                    is Msg.ServerInfoLoadingFailed -> copy(
                        instanceInfoState = InstanceInfoState.ERROR,
                        instanceInfo = Instance.empty(),
                    )

                    is Msg.LearnMoreVisibilityChanged -> copy(
                        learnMoreVisible = msg.visible,
                    )

                    is Msg.CredentialsStateChanged -> copy(
                        credentialsState = msg.newState,
                    )

                    is Msg.OAuthFlowStateChanged -> copy(
                        oauthFlowActive = msg.active,
                    )
                }
            }
        ) {}

    private sealed interface Action {
        data object CheckCurrentCredentials : Action
        data class StartOAuthFlow(val credentials: ObtainedCredentials) : Action
    }

    private sealed interface Msg {
        data class TextInput(val text: String) : Msg
        data object ServerInfoLoadingStarted : Msg
        data class ServerInfoLoaded(val info: Instance) : Msg
        data object ServerInfoLoadingFailed : Msg
        data class LearnMoreVisibilityChanged(val visible: Boolean) : Msg
        data class CredentialsStateChanged(val newState: CredentialsState) : Msg
        data class OAuthFlowStateChanged(val active: Boolean) : Msg
    }
}
