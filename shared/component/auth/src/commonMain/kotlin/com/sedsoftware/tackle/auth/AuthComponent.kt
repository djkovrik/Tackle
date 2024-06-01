package com.sedsoftware.tackle.auth

import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.sedsoftware.tackle.auth.model.CredentialsState

interface AuthComponent : BackHandlerOwner {

    val model: Value<Model>

    fun onTextInput(text: String)

    fun onRetryButtonClick()

    fun onAuthenticateClick()

    fun onShowLearnMore()

    fun onHideLearnMore()

    fun onJoinMastodonClick()

    data class Model(
        val textInput: String,
        val serverName: String,
        val serverDescription: String,
        val serverUsers: Long,
        val isLoadingServerInfo: Boolean,
        val isServerInfoLoaded: Boolean,
        val isServerInfoError: Boolean,
        val isOauthFlowActive: Boolean,
        val isLearnMoreVisible: Boolean,
        val credentialsState: CredentialsState,
    )

    sealed class Output {
        data object NavigateToHomeScreen : Output()
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
