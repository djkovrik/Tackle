package com.sedsoftware.tackle.auth.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.auth.model.CredentialsState
import com.sedsoftware.tackle.auth.model.InstanceInfoState
import com.sedsoftware.tackle.auth.store.AuthStore.Intent
import com.sedsoftware.tackle.auth.store.AuthStore.Label
import com.sedsoftware.tackle.auth.store.AuthStore.State
import com.sedsoftware.tackle.domain.model.Instance

internal interface AuthStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class OnTextInput(val text: String) : Intent()
        data object OnRetryButtonClick : Intent()
        data object OnAuthenticateButtonClick : Intent()
        data class ShowLearnMore(val show: Boolean) : Intent()
    }

    data class State(
        val userInput: String = "",
        val instanceInfo: Instance = Instance.empty(),
        val learnMoreVisible: Boolean = false,
        val instanceUrl: String = "",
        val oauthFlowActive: Boolean = false,
        val instanceInfoState: InstanceInfoState = InstanceInfoState.IDLE,
        val credentialsState: CredentialsState = CredentialsState.NOT_CHECKED,
    )

    sealed class Label {
        data object NavigateToMainScreen : Label()
        data class ErrorCaught(val exception: Throwable) : Label()
    }
}
