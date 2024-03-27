package com.sedsoftware.tackle.auth.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.auth.domain.InstanceInfoChecker
import com.sedsoftware.tackle.auth.extension.normalizeInput
import com.sedsoftware.tackle.auth.extension.trimInput
import com.sedsoftware.tackle.auth.model.InstanceInfo
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
    private val checker: InstanceInfoChecker,
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

                        val url = state.lastUserInput
                        val trimmedUrl = url.trimInput()
                        val normalizedUrl = url.normalizeInput()

                        dispatch(Msg.OnTextInput(trimmedUrl))
                        dispatch(Msg.ServerInfoLoadingStarted)

                        unwrap(
                            result = withContext(ioContext) { checker.getInstanceInfo(normalizedUrl) },
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

                onIntent<Intent.OnDefaultServerButtonClick> {
                    dispatch(Msg.OnDefaultServerClick)
                }

                onIntent<Intent.OnAuthenticateButtonClick> {
                    dispatch(Msg.OnAuthenticateClick)
                }

                onIntent<Intent.OAuthFlowCompleted> {
                    dispatch(Msg.OAuthFlowCompleted)
                }

                onIntent<Intent.OAuthFlowFailed> {
                    dispatch(Msg.OAuthFlowFailed)
                }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.OnTextInput -> copy(
                        lastUserInput = msg.text,
                    )

                    is Msg.OnDefaultServerClick -> copy(
                        lastUserInput = DEFAULT_SERVER,
                    )

                    is Msg.OnAuthenticateClick -> copy(
                        awaitingForOauth = true,
                    )

                    is Msg.ServerInfoLoadingStarted -> copy(
                        retrievingServerInfo = true,
                    )

                    is Msg.ServerInfoLoaded -> copy(
                        retrievingServerInfo = false,
                        currentServer = msg.info,
                    )

                    is Msg.ServerInfoLoadingFailed -> copy(
                        retrievingServerInfo = false,
                        currentServer = InstanceInfo.empty(),
                    )

                    is Msg.OAuthFlowCompleted -> copy(
                        awaitingForOauth = false,
                        authenticationCompleted = true,
                    )

                    is Msg.OAuthFlowFailed -> copy(
                        awaitingForOauth = false,
                        authenticationCompleted = false,
                    )
                }
            }
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class OnTextInput(val text: String) : Msg
        data object OnDefaultServerClick : Msg
        data object OnAuthenticateClick : Msg
        data object ServerInfoLoadingStarted : Msg
        data class ServerInfoLoaded(val info: InstanceInfo) : Msg
        data object ServerInfoLoadingFailed : Msg
        data object OAuthFlowCompleted : Msg
        data object OAuthFlowFailed : Msg
    }

    private companion object {
        const val DEFAULT_SERVER = "mastodon.social"
        const val INPUT_ENDED_DELAY = 2000L
    }
}
