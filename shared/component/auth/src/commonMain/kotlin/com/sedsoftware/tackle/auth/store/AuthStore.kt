package com.sedsoftware.tackle.auth.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.auth.model.CredentialsInfoState
import com.sedsoftware.tackle.auth.model.InstanceInfo
import com.sedsoftware.tackle.auth.model.InstanceInfoState
import com.sedsoftware.tackle.auth.store.AuthStore.Intent
import com.sedsoftware.tackle.auth.store.AuthStore.Label
import com.sedsoftware.tackle.auth.store.AuthStore.State

internal interface AuthStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class OnTextInput(val text: String) : Intent()
        data object OnRetryButtonClick : Intent()
        data class ShowLearnMore(val show: Boolean) : Intent()
    }

    data class State(
        val userInput: String = "",
        val instanceInfo: InstanceInfo = InstanceInfo.empty(),
        val learnMoreVisible: Boolean = false,
        val instanceUrl: String = "",
        val oauthFlowActive: Boolean = false,
        val instanceInfoState: InstanceInfoState = InstanceInfoState.IDLE,
        val credentialsInfoState: CredentialsInfoState = CredentialsInfoState.NOT_CHECKED,
    )

    sealed class Label {
        data object NavigateToHomeScreen : Label()
        data class ErrorCaught(val throwable: Throwable) : Label()
    }
}
