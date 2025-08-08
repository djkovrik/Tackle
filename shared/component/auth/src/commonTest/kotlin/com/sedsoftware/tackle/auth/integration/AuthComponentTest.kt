package com.sedsoftware.tackle.auth.integration

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotEmpty
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.auth.Constants
import com.sedsoftware.tackle.auth.Responses
import com.sedsoftware.tackle.auth.model.CredentialsState
import com.sedsoftware.tackle.auth.stubs.AuthComponentSettingsStub
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.utils.test.ComponentTest
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class AuthComponentTest : ComponentTest<AuthComponent>() {

    private val api: AuthComponentGateways.Api = mock {
        everySuspend { getServerInfo(any()) } returns Responses.validInstanceDetails
        everySuspend { createApp(any()) } returns Responses.validApplicationDetails
        everySuspend { startAuthFlow(any(), any(), any(), any()) } returns Responses.TOKEN
        everySuspend { verifyCredentials() } returns Responses.validAccountDetails
    }

    private val database: AuthComponentGateways.Database = mock {
        everySuspend { cacheInstanceInfo(any()) } returns Unit
    }

    private val settings: AuthComponentGateways.Settings = AuthComponentSettingsStub()

    private val tools: AuthComponentGateways.Tools = mock {
        everySuspend { getClientData() } returns Responses.validClientData
        everySuspend { openUrl(any()) } returns Unit
        everySuspend { getTextInputEndDelay() } returns 0L
    }

    private val activeModel: AuthComponent.Model
        get() = component.model.value

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
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
        assertThat(componentOutput).contains(ComponentOutput.Auth.AuthFlowCompleted)
    }

    @Test
    fun `api exceptions should call related output`() = runTest {
        // given
        val exception = IllegalStateException("Test")
        val url = "mastodon.social"
        everySuspend { api.getServerInfo(any()) } throws exception
        everySuspend { api.createApp(any()) } throws exception
        everySuspend { api.startAuthFlow(any(), any(), any(), any()) } throws exception
        everySuspend { api.verifyCredentials() } throws exception
        asUnauthorized()
        // when
        component.onTextInput(url)
        component.onAuthenticateClick()
        // then
        assertThat(componentOutput).isNotEmpty()
        assertThat(componentOutput.count { it is ComponentOutput.Common.ErrorCaught }).isGreaterThan(0)
    }

    override fun createComponent(): AuthComponent =
        AuthComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            api = api,
            database = database,
            settings = settings,
            tools = tools,
            dispatchers = testDispatchers,
            authOutput = { componentOutput.add(it) }
        )

    private fun asAuthorized() {
        settings.domainNormalized = Constants.DOMAIN
        settings.token = Constants.TOKEN
    }

    private fun asUnauthorized() {
        settings.domainNormalized = Constants.DOMAIN
        settings.token = ""
    }
}
