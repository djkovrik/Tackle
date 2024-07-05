package com.sedsoftware.tackle.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isSameInstanceAs
import com.sedsoftware.tackle.auth.model.ObtainedCredentials
import com.sedsoftware.tackle.auth.stubs.AuthComponentApiStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentDatabaseStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentSettingsStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentToolsStub
import com.sedsoftware.tackle.auth.stubs.StubConstants
import com.sedsoftware.tackle.domain.TackleException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class AuthFlowManagerTest {

    private val api: AuthComponentApiStub = AuthComponentApiStub()
    private val database: AuthComponentDatabaseStub = AuthComponentDatabaseStub()
    private val settings: AuthComponentSettingsStub = AuthComponentSettingsStub()
    private val tools: AuthComponentToolsStub = AuthComponentToolsStub()
    private val manager: AuthFlowManager = AuthFlowManager(api, database, settings, tools)

    @Test
    fun `getInstanceInfo should load instance info`() = runTest {
        // when
        val result = manager.getInstanceInfo(StubConstants.DOMAIN)
        // then
        assertThat(result.isSuccess, "Instance info")
    }

    @Test
    fun `verifyCredentials should return true for valid settings`() = runTest {
        // given
        settings.domainNormalized = StubConstants.DOMAIN
        settings.token = StubConstants.TOKEN
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
        settings.token = StubConstants.TOKEN
        // when
        val result = manager.verifyCredentials()
        // then
        assertThat(result.exceptionOrNull()).isSameInstanceAs(TackleException.MissedRegistrationData)
    }

    @Test
    fun `verifyCredentials should throw an exception if token is empty`() = runTest {
        // given
        settings.domainNormalized = StubConstants.DOMAIN
        settings.token = ""
        // when
        val result = manager.verifyCredentials()
        // then
        assertThat(result.exceptionOrNull()).isSameInstanceAs(TackleException.MissedRegistrationData)
    }

    @Test
    fun `createApp should update id and secret settings on success`() = runTest {
        // given
        settings.domainNormalized = ""
        settings.clientId = ""
        settings.clientSecret = ""
        api.createAppResponse = AuthComponentApiStub.validApplicationDetails
        // when
        val result = manager.createApp(StubConstants.DOMAIN)
        // then
        assertThat(result.isSuccess, "Successful credentials request")
        assertThat(settings.domainNormalized, "domain").isEqualTo(StubConstants.DOMAIN)
        assertThat(settings.clientId, "client_id").isNotEmpty()
        assertThat(settings.clientSecret, "client_secret").isNotEmpty()
    }

    @Test
    fun `startAuthFlow should return true on valid response`() = runTest {
        // given
        val credentials = ObtainedCredentials(
            clientId = StubConstants.CLIENT_ID,
            clientSecret = StubConstants.CLIENT_SECRET,
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
            clientId = StubConstants.CLIENT_ID,
            clientSecret = StubConstants.CLIENT_SECRET,
        )
        api.startAuthFlowResponse = ""
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
            clientId = StubConstants.CLIENT_ID,
            clientSecret = StubConstants.CLIENT_SECRET,
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
