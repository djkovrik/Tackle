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
                onIntent<Intent.OnTextInput> {
                    dispatch(Msg.OnTextInput(it.text))
                }

                onIntent<Intent.OnDefaultServerButtonClick> {
                    dispatch(Msg.OnTextInput(DEFAULT_SERVER))
                }

                onIntent<Intent.OnAuthenticateButtonClick> {
                    val url = state.textInput
                    val trimmedUrl = url.trimInput()
                    val normalizedUrl = url.normalizeInput()

                    dispatch(Msg.OnTextInput(trimmedUrl))
                    dispatch(Msg.ServerInfoLoadStarted(true))

                    launch {
                        unwrap(
                            result = withContext(ioContext) { checker.getInstanceInfo(normalizedUrl) },
                            onSuccess = {
                                dispatch(Msg.ServerInfoLoaded(this))
                            },
                            onError = {
                                dispatch(Msg.ServerInfoLoadingFailed)
                                publish(Label.ErrorCaught(this))
                            },
                        )
                    }
                }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.OnTextInput -> TODO()
                    is Msg.OnDefaultServerClick -> TODO()
                    is Msg.OnAuthenticateClick -> TODO()
                    is Msg.ServerInfoLoadStarted -> TODO()
                    is Msg.ServerInfoLoaded -> TODO()
                    is Msg.ServerInfoLoadingFailed -> TODO()
                }
            }
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class OnTextInput(val text: String) : Msg
        data object OnDefaultServerClick : Msg
        data object OnAuthenticateClick : Msg
        data class ServerInfoLoadStarted(val active: Boolean) : Msg
        data class ServerInfoLoaded(val info: InstanceInfo) : Msg
        data object ServerInfoLoadingFailed : Msg
    }

    private companion object {
        const val DEFAULT_SERVER = "mastodon.social"
    }
}

fun <T> unwrap(result: Result<T>, onSuccess: T.() -> Unit, onError: Throwable.() -> Unit) {
    with(result) {
        getOrNull()?.let { onSuccess(it) }
        exceptionOrNull()?.let { onError(it) }
    }
}

fun <T> ifError(result: Result<T>, block: Throwable.() -> Unit) {

}
