package com.sedsoftware.tackle.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isSameInstanceAs
import com.sedsoftware.tackle.auth.model.ObtainedCredentials
import com.sedsoftware.tackle.auth.stubs.AuthComponentApiStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentSettingsStub
import com.sedsoftware.tackle.auth.stubs.AuthComponentToolsStub
import com.sedsoftware.tackle.auth.stubs.StubConstants
import com.sedsoftware.tackle.utils.AppCreationException
import com.sedsoftware.tackle.utils.MissedRegistrationDataException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class AuthFlowManagerTest {

    private val api: AuthComponentApiStub = AuthComponentApiStub()
    private val settings: AuthComponentSettingsStub = AuthComponentSettingsStub()

    private val manager: AuthFlowManager = AuthFlowManager(
        tools = AuthComponentToolsStub(),
        api = api,
        settings = settings,
    )

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
        settings.domain = StubConstants.DOMAIN
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
        settings.domain = ""
        settings.token = StubConstants.TOKEN
        // when
        val result = manager.verifyCredentials()
        // then
        assertThat(result.exceptionOrNull()).isSameInstanceAs(MissedRegistrationDataException)
    }

    @Test
    fun `verifyCredentials should throw an exception if token is empty`() = runTest {
        // given
        settings.domain = StubConstants.DOMAIN
        settings.token = ""
        // when
        val result = manager.verifyCredentials()
        // then
        assertThat(result.exceptionOrNull()).isSameInstanceAs(MissedRegistrationDataException)
    }

    @Test
    fun `createApp should throw an exception on invalid response`() = runTest {
        // given
        api.createAppResponse = AuthComponentApiStub.invalidApplicationDetails
        // when
        val result = manager.createApp(StubConstants.DOMAIN)
        // then
        assertThat(result.exceptionOrNull()).isSameInstanceAs(AppCreationException)
    }

    @Test
    fun `createApp should update id and secret settings on success`() = runTest {
        // given
        settings.domain = ""
        settings.clientId = ""
        settings.clientSecret = ""
        // when
        val result = manager.createApp(StubConstants.DOMAIN)
        // then
        assertThat(result.isSuccess, "Successful credentials request")
        assertThat(settings.domain, "domain").isEqualTo(StubConstants.DOMAIN)
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
