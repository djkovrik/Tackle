package com.sedsoftware.tackle.auth.store

import com.sedsoftware.tackle.auth.model.InstanceInfo

internal interface AuthStore {

    sealed class Intent {
        data class OnTextInput(val text: String) : Intent()
        data object OnDefaultServerButtonClick : Intent()
        data object OnAuthenticateButtonClick : Intent()
    }

    data class State(
        val textInput: String = "",
        val isCheckingActive: Boolean = false,
        val isOauthFlowActive: Boolean = false,
        val currentServer: InstanceInfo = InstanceInfo.empty(),
    )

    sealed class Label {
        data class ErrorCaught(val throwable: Throwable?) : Label()
    }
}
