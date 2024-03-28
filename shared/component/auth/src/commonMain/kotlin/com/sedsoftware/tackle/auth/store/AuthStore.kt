package com.sedsoftware.tackle.auth.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.auth.model.InstanceInfo
import com.sedsoftware.tackle.auth.store.AuthStore.Intent
import com.sedsoftware.tackle.auth.store.AuthStore.Label
import com.sedsoftware.tackle.auth.store.AuthStore.State

internal interface AuthStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class OnTextInput(val text: String) : Intent()
        data object OnDefaultServerClick : Intent()
        data object OnAuthenticateClick : Intent()
        data object OAuthFlowCompleted : Intent()
        data object OAuthFlowFailed : Intent()
    }

    data class State(
        val userInput: String = "",
        val loadingServerInfo: Boolean = false,
        val serverInfo: InstanceInfo = InstanceInfo.empty(),
        val awaitingForOauth: Boolean = false,
        val authenticated: Boolean = false,
    )

    sealed class Label {
        data class ErrorCaught(val throwable: Throwable) : Label()
    }
}
