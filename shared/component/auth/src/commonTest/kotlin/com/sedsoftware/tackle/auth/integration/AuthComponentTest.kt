package com.sedsoftware.tackle.auth.integration

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.auth.model.CredentialsState
import com.sedsoftware.tackle.auth.stubs.AuthComponentApiStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentSettingsStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentToolsStub
import com.sedsoftware.tackle.auth.stubs.StubConstants
import com.sedsoftware.tackle.utils.test.ComponentTest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class AuthComponentTest : ComponentTest<AuthComponentDefault>() {

    private val settings: AuthComponentGateways.Settings = AuthComponentSettingsStub()

    private val activeModel: AuthComponent.Model
        get() = component.model.value

    @Test
    fun `when created go AUTHORIZED if token is available`() = runTest {
        // given
        asAuthorized()
        // when
        component = createComponent()
        // then
        assertThat(activeModel.credentialsState).isEqualTo(CredentialsState.AUTHORIZED)
    }

    @Test
    fun `when created go UNAUTHORIZED if token is not available`() = runTest {
        // given
        asUnauthorized()
        // when
        component = createComponent()
        // then
        assertThat(activeModel.credentialsState).isEqualTo(CredentialsState.UNAUTHORIZED)
    }

    @Test
    fun `onTextInput calls for server info load`() = runTest {
        // given
        val url = "mastodon.social"
        asUnauthorized()
        // when
        component = createComponent()
        component.onTextInput(url)
        // then
        assertThat(activeModel.credentialsState).isEqualTo(CredentialsState.UNAUTHORIZED)
        assertThat(activeModel.textInput).isEqualTo(url)
        assertThat(activeModel.serverName).isNotEmpty()
        assertThat(activeModel.serverDescription).isNotEmpty()
        assertThat(activeModel.isServerInfoLoaded).isEqualTo(true)
    }

    @Test
    fun `onShowLearnMore shows bottom sheet`() {
        // given
        asUnauthorized()
        // when
        component = createComponent()
        component.onShowLearnMore()
        // then
        assertThat(activeModel.isLearnMoreVisible).isEqualTo(true)
    }

    @Test
    fun `onHideLearnMore hides bottom sheet`() {
        // given
        asUnauthorized()
        // when
        component = createComponent()
        component.onShowLearnMore()
        // then
        assertThat(activeModel.isLearnMoreVisible).isEqualTo(true)
        // and when
        component.onHideLearnMore()
        // then
        assertThat(activeModel.isLearnMoreVisible).isEqualTo(false)
    }

    @Test
    fun `onAuthenticateClick runs auth flow`() {
        // given
        val url = "mastodon.social"
        asUnauthorized()
        // when
        component = createComponent()
        component.onTextInput(url)
        component.onAuthenticateClick()
        // then
        assertThat(activeModel.credentialsState).isEqualTo(CredentialsState.AUTHORIZED)
        assertThat(settings.token).isNotEmpty()
    }

    override fun createComponent(): AuthComponentDefault =
        AuthComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            api = AuthComponentApiStub(),
            settings = settings,
            tools = AuthComponentToolsStub(),
            dispatchers = testDispatchers,
            output = {}
        )

    private fun asAuthorized() {
        settings.domain = StubConstants.DOMAIN
        settings.token = StubConstants.TOKEN
    }

    private fun asUnauthorized() {
        settings.domain = StubConstants.DOMAIN
        settings.token = ""
    }
}
