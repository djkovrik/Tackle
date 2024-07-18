package com.sedsoftware.tackle.auth.integration

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.model.CredentialsState
import com.sedsoftware.tackle.auth.stubs.AuthComponentApiStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentDatabaseStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentSettingsStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentToolsStub
import com.sedsoftware.tackle.auth.stubs.StubConstants
import com.sedsoftware.tackle.utils.test.ComponentTest
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class AuthComponentTest : ComponentTest<AuthComponent>() {

    private val settings: AuthComponentSettingsStub = AuthComponentSettingsStub()

    private val activeModel: AuthComponent.Model
        get() = component.model.value

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    @Test
    fun `component creation should switch state to UNAUTHORIZED if token is not available`() = runTest {
        // given
        asUnauthorized()
        // when
        // then
        assertThat(activeModel.credentialsState).isEqualTo(CredentialsState.UNAUTHORIZED)
    }

    @Test
    fun `onTextInput should call for server info load`() = runTest {
        // given
        val url = "mastodon.social"
        asUnauthorized()
        // when
        component.onTextInput(url)
        // then
        assertThat(activeModel.credentialsState).isEqualTo(CredentialsState.UNAUTHORIZED)
        assertThat(activeModel.textInput).isEqualTo(url)
        assertThat(activeModel.serverName).isNotEmpty()
        assertThat(activeModel.serverDescription).isNotEmpty()
        assertThat(activeModel.isServerInfoLoaded).isEqualTo(true)
    }

    @Test
    fun `onRetryButtonClick should proceed with retrying`() = runTest {
        asUnauthorized()
        // when
        // then
        assertThat(activeModel.credentialsState).isEqualTo(CredentialsState.UNAUTHORIZED)
        // and when
        asAuthorized()
        component.onRetryButtonClick()
        assertThat(activeModel.credentialsState).isEqualTo(CredentialsState.AUTHORIZED)
    }

    @Test
    fun `onShowLearnMore should show bottom sheet`() {
        // given
        asUnauthorized()
        // when
        component.onShowLearnMore()
        // then
        assertThat(activeModel.isLearnMoreVisible).isEqualTo(true)
    }

    @Test
    fun `onHideLearnMore should hide bottom sheet`() {
        // given
        asUnauthorized()
        // when
        component.onShowLearnMore()
        // then
        assertThat(activeModel.isLearnMoreVisible).isEqualTo(true)
        // and when
        component.onHideLearnMore()
        // then
        assertThat(activeModel.isLearnMoreVisible).isEqualTo(false)
    }

    @Test
    fun `onJoinMastodonClick should hide bottom sheet`() {
        // given
        asUnauthorized()
        // when
        component.onShowLearnMore()
        // then
        assertThat(activeModel.isLearnMoreVisible).isEqualTo(true)
        // and when
        component.onJoinMastodonClick()
        // then
        assertThat(activeModel.isLearnMoreVisible).isEqualTo(false)
    }

    @Test
    fun `onAuthenticateClick should run auth flow`() {
        // given
        val url = "mastodon.social"
        asUnauthorized()
        // when
        component.onTextInput(url)
        component.onAuthenticateClick()
        // then
        assertThat(activeModel.credentialsState).isEqualTo(CredentialsState.AUTHORIZED)
        assertThat(settings.token).isNotEmpty()
    }

    override fun createComponent(): AuthComponent =
        AuthComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            api = AuthComponentApiStub(),
            database = AuthComponentDatabaseStub(),
            settings = settings,
            tools = AuthComponentToolsStub(),
            dispatchers = testDispatchers,
            output = {}
        )

    private fun asAuthorized() {
        settings.domainNormalized = StubConstants.DOMAIN
        settings.token = StubConstants.TOKEN
    }

    private fun asUnauthorized() {
        settings.domainNormalized = StubConstants.DOMAIN
        settings.token = ""
    }
}
