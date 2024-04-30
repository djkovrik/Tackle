package com.sedsoftware.tackle.auth

import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner

interface AuthComponent : BackHandlerOwner {

    val model: Value<Model>

    fun onTextInput(text: String)

    fun onAuthenticateClick()

    fun authFlowCompleted()

    fun authFlowFailed()

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
    )

    sealed class Output {
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
