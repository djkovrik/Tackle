package com.sedsoftware.tackle.auth.stubs

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.network.model.Application
import com.sedsoftware.tackle.network.model.Instance
import com.sedsoftware.tackle.utils.model.AppClientData

class AuthComponentApiStub : AuthComponentGateways.Api {

    companion object {
        val validApplicationDetails = Application(
            name = StubConstants.NAME,
            website = StubConstants.WEBSITE,
            clientId = StubConstants.CLIENT_ID,
            clientSecret = StubConstants.CLIENT_SECRET,
        )

        val invalidApplicationDetails = Application(
            name = "",
            website = "",
            clientId = "",
            clientSecret = "",
        )

        val validInstanceDetails = Instance(
            domain = StubConstants.DOMAIN,
            title = StubConstants.DOMAIN,
            version = "1.0",
            sourceUrl = StubConstants.DOMAIN,
            description = StubConstants.NAME,
            activePerMonth = 123L,
            thumbnailUrl = StubConstants.DOMAIN,
            languages = listOf("en", "ru"),
        )

        val invalidInstanceDetails = Instance(
            domain = "",
            title = "",
            version = "1.0",
            sourceUrl = "",
            description = "",
            activePerMonth = 123L,
            thumbnailUrl = "",
            languages = emptyList(),
        )
    }

    var getServerInfoResponse: Instance = validInstanceDetails
    var createAppResponse: Application = validApplicationDetails
    var startAuthFlowResponse: String = StubConstants.TOKEN
    var verifyCredentialsResponse: Application = validApplicationDetails
    var shouldThrowException: Boolean = false

    override suspend fun getServerInfo(url: String): Instance =
        if (!shouldThrowException) {
            getServerInfoResponse
        } else {
            error("getServerInfo exception")
        }

    override suspend fun createApp(data: AppClientData): Application =
        if (!shouldThrowException) {
            createAppResponse
        } else {
            error("createApp exception")
        }

    override suspend fun startAuthFlow(id: String, secret: String, uri: String, scopes: String): String =
        if (!shouldThrowException) {
            startAuthFlowResponse
        } else {
            error("startAuthFlow exception")
        }

    override suspend fun verifyCredentials(): Application =
        if (!shouldThrowException) {
            verifyCredentialsResponse
        } else {
            error("verifyCredentials exception")
        }
}
