package com.sedsoftware.tackle.auth.store

import com.sedsoftware.tackle.auth.model.InstanceInfo

internal interface AuthStore {

    sealed class Intent {
        data class OnTextInput(val text: String) : Intent()
        data object OnDefaultServerButtonClick : Intent()
        data object OnAuthenticateButtonClick : Intent()
        data object OAuthFlowCompleted : Intent()
        data object OAuthFlowFailed : Intent()
    }

    data class State(
        val lastUserInput: String = "",
        val lastUserInputTimestamp: Long = -1L,
        val retrievingServerInfo: Boolean = false,
        val currentServer: InstanceInfo = InstanceInfo.empty(),
        val awaitingForOauth: Boolean = false,
        val authenticationCompleted: Boolean = false,
    )

    sealed class Label {
        data class ErrorCaught(val throwable: Throwable?) : Label()
    }
}
