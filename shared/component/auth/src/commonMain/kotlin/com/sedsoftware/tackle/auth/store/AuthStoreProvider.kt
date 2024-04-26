package com.sedsoftware.tackle.auth.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.auth.domain.InstanceInfoManager
import com.sedsoftware.tackle.auth.extension.isValidUrl
import com.sedsoftware.tackle.auth.extension.normalizeForRequest
import com.sedsoftware.tackle.auth.extension.trimForDisplaying
import com.sedsoftware.tackle.auth.model.InstanceInfo
import com.sedsoftware.tackle.auth.model.InstanceInfoState
import com.sedsoftware.tackle.auth.store.AuthStore.Intent
import com.sedsoftware.tackle.auth.store.AuthStore.Label
import com.sedsoftware.tackle.auth.store.AuthStore.State
import com.sedsoftware.tackle.utils.unwrap
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class AuthStoreProvider(
    private val storeFactory: StoreFactory,
    private val manager: InstanceInfoManager,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
) {

    @OptIn(ExperimentalMviKotlinApi::class)
    fun create(autoInit: Boolean = true): AuthStore =
        object : AuthStore, Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
            name = "AuthStore",
            initialState = State(),
            autoInit = autoInit,
            executorFactory = coroutineExecutorFactory(mainContext) {
                var job: Job? = null

                onIntent<Intent.OnTextInput> {
                    dispatch(Msg.OnTextInput(it.text))
                    job?.cancel()
                    job = launch {
                        delay(INPUT_ENDED_DELAY)

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

                onIntent<Intent.OnAuthenticateClick> {
                    dispatch(Msg.OnAuthenticateClick)
                }

                onIntent<Intent.OAuthFlowCompleted> {
                    dispatch(Msg.OAuthFlowCompleted)
                }

                onIntent<Intent.OAuthFlowFailed> {
                    dispatch(Msg.OAuthFlowFailed)
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

                    is Msg.OnAuthenticateClick -> copy(
                        awaitingForOauth = true,
                    )

                    is Msg.OAuthFlowCompleted -> copy(
                        awaitingForOauth = false,
                    )

                    is Msg.OAuthFlowFailed -> copy(
                        awaitingForOauth = false,
                    )

                    is Msg.LearnMoreVisibilityChanged -> copy(
                        learnMoreVisible = msg.visible,
                    )
                }
            }
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class OnTextInput(val text: String) : Msg
        data object ServerInfoLoadingStarted : Msg
        data class ServerInfoLoaded(val info: InstanceInfo) : Msg
        data object ServerInfoLoadingFailed : Msg
        data object OnAuthenticateClick : Msg
        data object OAuthFlowCompleted : Msg
        data object OAuthFlowFailed : Msg
        data class LearnMoreVisibilityChanged(val visible: Boolean) : Msg
    }

    private companion object {
        const val INPUT_ENDED_DELAY = 2000L
    }
}
