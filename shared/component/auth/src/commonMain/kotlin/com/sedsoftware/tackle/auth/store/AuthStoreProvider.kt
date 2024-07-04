package com.sedsoftware.tackle.auth.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.auth.domain.AuthFlowManager
import com.sedsoftware.tackle.auth.extension.isValidUrl
import com.sedsoftware.tackle.auth.extension.normalizeUrl
import com.sedsoftware.tackle.auth.model.CredentialsState
import com.sedsoftware.tackle.auth.model.CredentialsState.AUTHORIZED
import com.sedsoftware.tackle.auth.model.CredentialsState.EXISTING_USER_CHECK_FAILED
import com.sedsoftware.tackle.auth.model.CredentialsState.UNAUTHORIZED
import com.sedsoftware.tackle.auth.model.InstanceInfoState
import com.sedsoftware.tackle.auth.model.ObtainedCredentials
import com.sedsoftware.tackle.auth.store.AuthStore.Intent
import com.sedsoftware.tackle.auth.store.AuthStore.Label
import com.sedsoftware.tackle.auth.store.AuthStore.Label.ErrorCaught
import com.sedsoftware.tackle.auth.store.AuthStore.Label.NavigateToMainScreen
import com.sedsoftware.tackle.auth.store.AuthStore.State
import com.sedsoftware.tackle.auth.store.AuthStoreProvider.Action.CheckCurrentCredentials
import com.sedsoftware.tackle.auth.store.AuthStoreProvider.Action.StartOAuthFlow
import com.sedsoftware.tackle.auth.store.AuthStoreProvider.Msg.CredentialsStateChanged
import com.sedsoftware.tackle.auth.store.AuthStoreProvider.Msg.OAuthFlowStateChanged
import com.sedsoftware.tackle.auth.store.AuthStoreProvider.Msg.ServerInfoLoaded
import com.sedsoftware.tackle.auth.store.AuthStoreProvider.Msg.ServerInfoLoadingFailed
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.utils.StoreCreate
import com.sedsoftware.tackle.domain.TackleException.MissedRegistrationData
import com.sedsoftware.tackle.utils.extension.isUnauthorized
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
            bootstrapper = coroutineBootstrapper {
                dispatch(Action.CheckCurrentCredentials)
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
                var job: Job? = null

                onAction<Action.CheckCurrentCredentials> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.verifyCredentials() },
                            onSuccess = { isCredentialsValid ->
                                if (isCredentialsValid) {
                                    dispatch(CredentialsStateChanged(newState = AUTHORIZED))
                                    publish(NavigateToMainScreen)
                                } else {
                                    dispatch(CredentialsStateChanged(newState = UNAUTHORIZED))
                                }
                            },
                            onError = { throwable ->
                                if (throwable is MissedRegistrationData || throwable.isUnauthorized) {
                                    dispatch(CredentialsStateChanged(newState = UNAUTHORIZED))
                                } else {
                                    dispatch(CredentialsStateChanged(newState = EXISTING_USER_CHECK_FAILED))
                                }
                                publish(ErrorCaught(throwable))
                            },
                        )
                    }
                }

                onAction<Action.StartOAuthFlow> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.startAuthFlow(it.credentials) },
                            onSuccess = { isAuthorized ->
                                if (isAuthorized) {
                                    forward(CheckCurrentCredentials)
                                } else {
                                    dispatch(CredentialsStateChanged(newState = UNAUTHORIZED))
                                }
                            },
                            onError = { throwable ->
                                dispatch(OAuthFlowStateChanged(active = false))
                                publish(ErrorCaught(throwable))
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

                            val normalizedUrl = url.normalizeUrl()

                            unwrap(
                                result = withContext(ioContext) { manager.getInstanceInfo(normalizedUrl) },
                                onSuccess = { info ->
                                    dispatch(ServerInfoLoaded(info = info))
                                },
                                onError = { throwable ->
                                    dispatch(ServerInfoLoadingFailed)
                                    publish(ErrorCaught(throwable))
                                },
                            )
                        }
                    }
                }

                onIntent<Intent.OnRetryButtonClick> {
                    dispatch(Msg.CredentialsStateChanged(newState = CredentialsState.RETRYING))
                    forward(Action.CheckCurrentCredentials)
                }

                onIntent<Intent.OnAuthenticateButtonClick> {
                    dispatch(Msg.OAuthFlowStateChanged(active = true))
                    val domain = state().instanceInfo.domain

                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.createApp(domain) },
                            onSuccess = { credentials ->
                                forward(StartOAuthFlow(credentials))
                            },
                            onError = { throwable ->
                                dispatch(OAuthFlowStateChanged(active = false))
                                publish(ErrorCaught(throwable))
                            },
                        )
                    }
                }

                onIntent<Intent.ShowLearnMore> {
                    dispatch(Msg.LearnMoreVisibilityChanged(visible = it.show))
                }
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
