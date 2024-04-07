package com.sedsoftware.tackle.auth.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.backhandler.BackHandler
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.AuthComponent.Model

class AuthComponentPreview(
    textInput: String = "",
    isRetrievingServerInfo: Boolean = false,
    serverPreviewVisible: Boolean = false,
    isOauthFlowActive: Boolean = false,
    isAuthenticated: Boolean = false,
    isLearnMoreVisible: Boolean = false,
) : AuthComponent {

    override val backHandler: BackHandler =
        object : BackHandler {
            override fun register(callback: BackCallback) = Unit
            override fun unregister(callback: BackCallback) = Unit
        }

    override val model: Value<Model> =
        MutableValue(
            Model(
                textInput = textInput,
                isRetrievingServerInfo = isRetrievingServerInfo,
                serverPreviewVisible = serverPreviewVisible,
                serverName = "Mastodon",
                serverDescription = "The original server operated by the Mastodon gGmbH non-profit",
                serverUsers = 123123123,
                isOauthFlowActive = isOauthFlowActive,
                isAuthenticated = isAuthenticated,
                isLearnMoreVisible = isLearnMoreVisible,
            )
        )

    override fun onTextInput(text: String) = Unit
    override fun onAuthenticateClick() = Unit
    override fun authFlowCompleted() = Unit
    override fun authFlowFailed() = Unit
    override fun onShowLearnMore() = Unit
    override fun onHideLearnMore() = Unit
}
