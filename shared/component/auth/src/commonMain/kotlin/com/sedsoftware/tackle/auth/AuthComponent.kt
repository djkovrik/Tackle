package com.sedsoftware.tackle.auth

import com.arkivanov.decompose.value.Value

interface AuthComponent {

    val model: Value<Model>

    fun onAuthenticateClicked()

    fun onPopulateDefaultServerClicked()

    fun authFlowCompleted()

    fun authFlowFailed()

    data class Model(
        val textInput: String,
        val isRetrievingServerInfo: Boolean,
        val serverPreviewVisible: Boolean,
        val serverName: String,
        val serverDescription: String,
        val serverUsers: Long,
        val isOauthFlowActive: Boolean,
        val isAuthenticated: Boolean,
    )

    sealed class Output {
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
