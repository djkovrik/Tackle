package com.sedsoftware.tackle.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isSameInstanceAs
import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.auth.Constants
import com.sedsoftware.tackle.auth.Responses
import com.sedsoftware.tackle.auth.model.ObtainedCredentials
import com.sedsoftware.tackle.auth.stubs.AuthComponentSettingsStub
import com.sedsoftware.tackle.domain.TackleException
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class AuthFlowManagerTest {

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

    private val manager: AuthFlowManager = AuthFlowManager(api, database, settings, tools)

    @Test
    fun `getInstanceInfo should load instance info`() = runTest {
        // given
        val domain = Constants.DOMAIN
        // when
        val result = manager.getInstanceInfo(domain)
        // then
        assertThat(result.isSuccess, "Instance info")
    }

    @Test
    fun `verifyCredentials should return true for valid settings`() = runTest {
        // given
        settings.domainNormalized = Constants.DOMAIN
        settings.token = Constants.TOKEN
        // when
        val result = manager.verifyCredentials()
        // then
        assertThat(result.isSuccess, "Successful request")
        assertThat(result.getOrThrow()).isEqualTo(true)
    }

    @Test
    fun `verifyCredentials should throw an exception if domain is invalid`() = runTest {
        // given
        settings.domainNormalized = ""
        settings.token = Constants.TOKEN
        // when
        val result = manager.verifyCredentials()
        // then
        assertThat(result.exceptionOrNull()).isSameInstanceAs(TackleException.MissedRegistrationData)
    }

    @Test
    fun `verifyCredentials should throw an exception if token is empty`() = runTest {
        // given
        settings.domainNormalized = Constants.DOMAIN
        settings.token = ""
        // when
        val result = manager.verifyCredentials()
        // then
        assertThat(result.exceptionOrNull()).isSameInstanceAs(TackleException.MissedRegistrationData)
    }

    @Test
    fun `verifyCredentials should store current user info`() = runTest {
        // given
        settings.domainNormalized = Constants.DOMAIN
        settings.token = Constants.TOKEN
        // when
        manager.verifyCredentials()
        // then
        assertThat(settings.ownAvatar).isEqualTo(Constants.OWN_AVATAR)
        assertThat(settings.ownUsername).isEqualTo(Constants.OWN_USERNAME)
        assertThat(settings.ownId).isEqualTo(Constants.OWN_USER_ID)
    }

    @Test
    fun `createApp should update id and secret settings on success`() = runTest {
        // given
        settings.domainNormalized = ""
        settings.clientId = ""
        settings.clientSecret = ""
        everySuspend { api.createApp(any()) } returns Responses.validApplicationDetails
        // when
        val result = manager.createApp(Constants.DOMAIN)
        // then
        assertThat(result.isSuccess, "Successful credentials request")
        assertThat(settings.domainNormalized, "domain").isEqualTo(Constants.DOMAIN)
        assertThat(settings.clientId, "client_id").isNotEmpty()
        assertThat(settings.clientSecret, "client_secret").isNotEmpty()
    }

    @Test
    fun `startAuthFlow should return true on valid response`() = runTest {
        // given
        val credentials = ObtainedCredentials(
            clientId = Constants.CLIENT_ID,
            clientSecret = Constants.CLIENT_SECRET,
        )
        // when
        val result = manager.startAuthFlow(credentials)
        // then
        assertThat(result.isSuccess, "Successful request")
        assertThat(result.getOrThrow()).isEqualTo(true)
    }

    @Test
    fun `startAuthFlow should return false on invalid response`() = runTest {
        // given
        val credentials = ObtainedCredentials(
            clientId = Constants.CLIENT_ID,
            clientSecret = Constants.CLIENT_SECRET,
        )
        everySuspend { api.startAuthFlow(any(), any(), any(), any()) } returns ""
        // when
        val result = manager.startAuthFlow(credentials)
        // then
        assertThat(result.isSuccess, "Successful request")
        assertThat(result.getOrThrow()).isEqualTo(false)
    }

    @Test
    fun `startAuthFlow should update token setting on valid response`() = runTest {
        // given
        val credentials = ObtainedCredentials(
            clientId = Constants.CLIENT_ID,
            clientSecret = Constants.CLIENT_SECRET,
        )
        // when
        manager.startAuthFlow(credentials)
        // then
        assertThat(settings.token).isNotEmpty()
    }

    @Test
    fun `getTextInputEndDelay should return zero for tests`() {
        assertThat(manager.getTextInputEndDelay()).isEqualTo(0L)
    }
}
