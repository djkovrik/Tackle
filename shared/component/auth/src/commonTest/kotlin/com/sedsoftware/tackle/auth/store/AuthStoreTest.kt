package com.sedsoftware.tackle.auth.store

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.auth.domain.AuthFlowManager
import com.sedsoftware.tackle.auth.model.CredentialsState
import com.sedsoftware.tackle.auth.stubs.AuthComponentApiStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentSettingsStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentToolsStub
import com.sedsoftware.tackle.auth.stubs.StubConstants
import com.sedsoftware.tackle.utils.test.StoreTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class AuthStoreTest : StoreTest<AuthStore.Intent, AuthStore.State, AuthStore.Label>() {

    private val settings: AuthComponentGateways.Settings = AuthComponentSettingsStub()

    @Test
    fun `at launch go AUTHORIZED with navigation call if token is available`() = runTest {
        // given
        asAuthorized()
        // when
        store.init()
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.AUTHORIZED)
        assertThat(labels).contains(AuthStore.Label.NavigateToHomeScreen)
    }

    @Test
    fun `at launch go UNAUTHORIZED if token is not available`() = runTest {
        // given
        asUnauthorized()
        // when
        store.init()
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.UNAUTHORIZED)
        assertThat(labels).isEmpty()
    }

    @Test
    fun `OnTextInput updates input text`() {
        // given
        val text = "abcde"
        asUnauthorized()
        // when
        store.init()
        store.accept(AuthStore.Intent.OnTextInput(text))
        // then
        assertThat(store.state.userInput).isEqualTo(text)
    }

    @Test
    fun `OnRetryButtonClick updates state with retrying`() {
        // given
        asUnauthorized()
        // when
        store.init()
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.UNAUTHORIZED)
        // and when
        asAuthorized()
        store.accept(AuthStore.Intent.OnRetryButtonClick)
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.AUTHORIZED)
        assertThat(labels).contains(AuthStore.Label.NavigateToHomeScreen)
    }

    @Test
    fun `OnAuthenticateButtonClick runs auth flow`() {
        // given
        asUnauthorized()
        // when
        store.init()
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.UNAUTHORIZED)
        // and when
        asAuthorized()
        store.accept(AuthStore.Intent.OnAuthenticateButtonClick)
        // then
        assertThat(store.state.credentialsState, "Credentials state").isEqualTo(CredentialsState.AUTHORIZED)
        assertThat(labels).contains(AuthStore.Label.NavigateToHomeScreen)
    }

    @Test
    fun `ShowLearnMore updates bottom sheet visibility`() {
        // given
        asUnauthorized()
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
                api = AuthComponentApiStub(),
                tools = AuthComponentToolsStub(),
                settings = settings,
            ),
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
        ).create(autoInit = false)
    }

    private fun asAuthorized() {
        settings.domain = StubConstants.DOMAIN
        settings.token = StubConstants.TOKEN
    }

    private fun asUnauthorized() {
        settings.domain = StubConstants.DOMAIN
        settings.token = ""
    }
}
