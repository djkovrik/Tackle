package com.sedsoftware.tackle.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isSameInstanceAs
import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.auth.stubs.AuthComponentApiStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentSettingsStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentToolsStub
import com.sedsoftware.tackle.auth.stubs.StubConstants
import com.sedsoftware.tackle.utils.MissedRegistrationDataException
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class AuthFlowManagerTest {

    private val settings: AuthComponentGateways.Settings = AuthComponentSettingsStub()

    private val manager: AuthFlowManager = AuthFlowManager(
        api = AuthComponentApiStub(),
        tools = AuthComponentToolsStub(),
        settings = settings,
    )

    @Test
    fun `verifyCredentials validates credentials if domain and token are not empty`() = runBlocking {
        settings.domain = StubConstants.DOMAIN
        settings.token = StubConstants.TOKEN

        val result = manager.verifyCredentials()

        assertThat(result.isSuccess, "Successful validation")
        assertThat(result.getOrThrow()).isEqualTo(true)
    }

    @Test
    fun `verifyCredentials throws an exception if domain is invalid`() = runBlocking {
        settings.domain = ""
        settings.token = StubConstants.TOKEN

        val result = manager.verifyCredentials()

        assertThat(result.exceptionOrNull()).isSameInstanceAs(MissedRegistrationDataException)
    }

    @Test
    fun `verifyCredentials throws an exception if token is empty`() = runBlocking {
        settings.domain = StubConstants.DOMAIN
        settings.token = ""

        val result = manager.verifyCredentials()

        assertThat(result.exceptionOrNull()).isSameInstanceAs(MissedRegistrationDataException)
    }

    @Test
    fun `createApp updates id and secret settings`() = runBlocking {
        settings.domain = ""
        settings.clientId = ""
        settings.clientSecret = ""

        val result = manager.createApp(StubConstants.DOMAIN)

        assertThat(result.isSuccess, "Successful credentials request")
        assertThat(settings.domain, "domain").isEqualTo(StubConstants.DOMAIN)
        assertThat(settings.clientId, "client_id").isNotEmpty()
        assertThat(settings.clientSecret, "client_secret").isNotEmpty()
    }
}
