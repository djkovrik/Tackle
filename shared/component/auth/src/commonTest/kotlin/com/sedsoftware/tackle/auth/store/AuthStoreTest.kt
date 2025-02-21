package com.sedsoftware.tackle.auth.store

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.auth.Constants
import com.sedsoftware.tackle.auth.Responses
import com.sedsoftware.tackle.auth.domain.AuthFlowManager
import com.sedsoftware.tackle.auth.model.CredentialsState
import com.sedsoftware.tackle.auth.model.InstanceInfoState
import com.sedsoftware.tackle.auth.stubs.AuthComponentApiStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentDatabaseStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentSettingsStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentToolsStub
import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.utils.test.StoreTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class AuthStoreTest : StoreTest<AuthStore.Intent, AuthStore.State, AuthStore.Label>() {

    private val api: AuthComponentApiStub = AuthComponentApiStub()
    private val settings: AuthComponentSettingsStub = AuthComponentSettingsStub()

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `store creation should switch state to AUTHORIZED with navigation call if token is available`() = runTest {
        // given
        asAuthorized()
        api.responseWithException = false
        // when
        store.init()
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.AUTHORIZED)
        assertThat(labels).contains(AuthStore.Label.NavigateToMainScreen)
    }

    @Test
    fun `store creation should switch state to UNAUTHORIZED with navigation call if token expired`() = runTest {
        // given
        asAuthorized()
        api.verifyCredentialsResponse = Responses.invalidAccountDetails
        api.responseWithException = false
        // when
        store.init()
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.UNAUTHORIZED)
    }

    @Test
    fun `store creation should switch state to UNAUTHORIZED if token is not available`() = runTest {
        // given
        asUnauthorized()
        api.responseWithException = false
        // when
        store.init()
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.UNAUTHORIZED)
        assertThat(labels.first()).isEqualTo(AuthStore.Label.ErrorCaught(TackleException.MissedRegistrationData))
    }

    @Test
    fun `OnTextInput should update input text`() {
        // given
        val text = "abcde"
        asUnauthorized()
        api.responseWithException = false
        // when
        store.init()
        store.accept(AuthStore.Intent.OnTextInput(text))
        // then
        assertThat(store.state.userInput).isEqualTo(text)
    }

    @Test
    fun `OnTextInput should load server info`() {
        // given
        val text = "mastodon.social"
        asUnauthorized()
        api.responseWithException = false
        // when
        store.init()
        store.accept(AuthStore.Intent.OnTextInput(text))
        // then
        assertThat(store.state.instanceInfo.domain).isNotEmpty()
        assertThat(store.state.instanceInfo.title).isNotEmpty()
        assertThat(store.state.instanceInfo.description).isNotEmpty()
        assertThat(store.state.instanceInfo.thumbnailUrl).isNotEmpty()
    }

    @Test
    fun `broken server info from OnTextInput should update instance info state`() {
        // given
        val text = "mastodon.social"
        asUnauthorized()
        api.getServerInfoResponse = Responses.invalidInstanceDetails
        api.responseWithException = false
        // when
        store.init()
        store.accept(AuthStore.Intent.OnTextInput(text))
        // then
        assertThat(store.state.instanceInfoState).isEqualTo(InstanceInfoState.ERROR)
    }

    @Test
    fun `exception from OnTextInput should update instance info state`() {
        // given
        val text = "mastodon.social"
        asUnauthorized()
        api.getServerInfoResponse = Responses.invalidInstanceDetails
        api.responseWithException = true
        // when
        store.init()
        store.accept(AuthStore.Intent.OnTextInput(text))
        // then
        assertThat(store.state.instanceInfoState).isEqualTo(InstanceInfoState.ERROR)
    }

    @Test
    fun `OnRetryButtonClick should update state with retrying`() {
        // given
        asUnauthorized()
        api.responseWithException = false
        // when
        store.init()
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.UNAUTHORIZED)
        // and when
        asAuthorized()
        store.accept(AuthStore.Intent.OnRetryButtonClick)
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.AUTHORIZED)
        assertThat(labels).contains(AuthStore.Label.NavigateToMainScreen)
    }

    @Test
    fun `OnAuthenticateButtonClick should run auth flow`() {
        // given
        asUnauthorized()
        api.responseWithException = false
        // when
        store.init()
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.UNAUTHORIZED)
        // and when
        asAuthorized()
        store.accept(AuthStore.Intent.OnAuthenticateButtonClick)
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.AUTHORIZED)
        assertThat(labels).contains(AuthStore.Label.NavigateToMainScreen)
    }

    @Test
    fun `OnAuthenticateButtonClick should show an error if api throws an exception`() {
        // given
        asUnauthorized()
        api.responseWithException = true
        // when
        store.init()
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.UNAUTHORIZED)
        // and when
        asAuthorized()
        store.accept(AuthStore.Intent.OnAuthenticateButtonClick)
        // then
        assertThat(store.state.oauthFlowActive, "Credentials state").isEqualTo(false)
        assertThat(labels).isNotEmpty()
    }

    @Test
    fun `ShowLearnMore should update bottom sheet visibility`() {
        // given
        asUnauthorized()
        api.responseWithException = false
        // when
        store.init()
        store.accept(AuthStore.Intent.ShowLearnMore(true))
        // then
        assertThat(store.state.learnMoreVisible).isEqualTo(true)
        // and when
        store.accept(AuthStore.Intent.ShowLearnMore(false))
        // then
        assertThat(store.state.learnMoreVisible).isEqualTo(false)
    }

    override fun createStore(): AuthStore {
        return AuthStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = AuthFlowManager(
                tools = AuthComponentToolsStub(),
                api = api,
                database = AuthComponentDatabaseStub(),
                settings = settings,
            ),
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
        ).create(autoInit = false)
    }

    private fun asAuthorized() {
        settings.domainNormalized = Constants.DOMAIN
        settings.token = Constants.TOKEN
    }

    private fun asUnauthorized() {
        settings.domainNormalized = Constants.DOMAIN
        settings.token = ""
    }
}
