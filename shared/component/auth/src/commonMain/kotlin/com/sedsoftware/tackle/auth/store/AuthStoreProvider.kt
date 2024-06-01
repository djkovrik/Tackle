package com.sedsoftware.tackle.auth.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.auth.domain.AuthFlowManager
import com.sedsoftware.tackle.auth.extension.isValidUrl
import com.sedsoftware.tackle.auth.extension.normalizeForRequest
import com.sedsoftware.tackle.auth.extension.trimForDisplaying
import com.sedsoftware.tackle.auth.model.CredentialsInfoState
import com.sedsoftware.tackle.auth.model.InstanceInfo
import com.sedsoftware.tackle.auth.model.InstanceInfoState
import com.sedsoftware.tackle.auth.store.AuthStore.Intent
import com.sedsoftware.tackle.auth.store.AuthStore.Label
import com.sedsoftware.tackle.auth.store.AuthStore.State
import com.sedsoftware.tackle.utils.MissedRegistrationDataException
import com.sedsoftware.tackle.utils.unwrap
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
                                    dispatch(Msg.CredentialsStateChanged(CredentialsInfoState.AUTHORIZED))
                                    publish(Label.NavigateToHomeScreen)
                                } else {
                                    dispatch(Msg.CredentialsStateChanged(CredentialsInfoState.UNAUTHORIZED))
                                }
                            },
                            onError = { throwable ->
                                if (throwable is MissedRegistrationDataException) {
                                    dispatch(Msg.CredentialsStateChanged(CredentialsInfoState.UNAUTHORIZED))
                                } else {
                                    dispatch(Msg.CredentialsStateChanged(CredentialsInfoState.EXISTING_USER_CHECK_FAILED))
                                }
                            }
                        )
                    }
                }

                onIntent<Intent.OnTextInput> {
                    dispatch(Msg.OnTextInput(it.text))
                    job?.cancel()
                    job = launch {
                        delay(INPUT_ENDED_DELAY)

                        val state = state()
                        val url = state.userInput
                        val displayedUrl = url.trimForDisplaying()

                        dispatch(Msg.OnTextInput(displayedUrl))

                        if (displayedUrl.isValidUrl()) {
                            dispatch(Msg.ServerInfoLoadingStarted)

                            val normalizedUrl = url.normalizeForRequest()

                            unwrap(
                                result = withContext(ioContext) { manager.getInstanceInfo(normalizedUrl) },
                                onSuccess = { info ->
                                    dispatch(Msg.ServerInfoLoaded(info))
                                },
                                onError = { throwable ->
                                    dispatch(Msg.ServerInfoLoadingFailed)
                                    publish(Label.ErrorCaught(throwable))
                                },
                            )
                        }
                    }
                }

                onIntent<Intent.OnRetryButtonClick> {
                    dispatch(Msg.CredentialsStateChanged(CredentialsInfoState.RETRYING))
                    forward(Action.CheckCurrentCredentials)
                }

                onIntent<Intent.ShowLearnMore> {
                    dispatch(Msg.LearnMoreVisibilityChanged(it.show))
                }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.OnTextInput -> copy(
                        userInput = msg.text,
                        instanceInfoState = InstanceInfoState.IDLE,
                    )

                    is Msg.ServerInfoLoadingStarted -> copy(
                        instanceInfoState = InstanceInfoState.LOADING,
                    )

                    is Msg.ServerInfoLoaded -> copy(
                        instanceInfo = msg.info,
                        instanceInfoState = if (msg.info.name.isNotEmpty()) {
                            InstanceInfoState.LOADED
                        } else {
                            InstanceInfoState.ERROR
                        },
                    )

                    is Msg.ServerInfoLoadingFailed -> copy(
                        instanceInfoState = InstanceInfoState.ERROR,
                        instanceInfo = InstanceInfo.empty(),
                    )

                    is Msg.LearnMoreVisibilityChanged -> copy(
                        learnMoreVisible = msg.visible,
                    )

                    is Msg.CredentialsStateChanged -> copy(
                        credentialsInfoState = msg.newState,
                    )
                }
            }
        ) {}

    private sealed interface Action {
        data object CheckCurrentCredentials : Action
    }

    private sealed interface Msg {
        data class OnTextInput(val text: String) : Msg
        data object ServerInfoLoadingStarted : Msg
        data class ServerInfoLoaded(val info: InstanceInfo) : Msg
        data object ServerInfoLoadingFailed : Msg
        data class LearnMoreVisibilityChanged(val visible: Boolean) : Msg
        data class CredentialsStateChanged(val newState: CredentialsInfoState) : Msg
    }

    private companion object {
        const val INPUT_ENDED_DELAY = 2000L
    }
}
