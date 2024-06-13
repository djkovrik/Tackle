package com.sedsoftware.tackle.auth.stubs

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.network.response.ApplicationDetails
import com.sedsoftware.tackle.network.response.InstanceActiveMonth
import com.sedsoftware.tackle.network.response.InstanceDetails
import com.sedsoftware.tackle.network.response.InstanceThumbnail
import com.sedsoftware.tackle.network.response.InstanceUsers
import com.sedsoftware.tackle.utils.model.AppClientData

class AuthComponentApiStub : AuthComponentGateways.Api {

    override suspend fun getServerInfo(url: String): InstanceDetails =
        InstanceDetails(
            domain = StubConstants.DOMAIN,
            title = StubConstants.DOMAIN,
            version = "1.0",
            sourceUrl = StubConstants.DOMAIN,
            description = StubConstants.NAME,
            usage = InstanceUsers(users = InstanceActiveMonth(activePerMonth = 123456L)),
            thumbnail = InstanceThumbnail(url = StubConstants.DOMAIN, blurHash = "", versions = emptyMap()),
            languages = listOf("en", "ru")
        )

    override suspend fun createApp(data: AppClientData): ApplicationDetails =
        ApplicationDetails(
            name = StubConstants.NAME,
            website = StubConstants.WEBSITE,
            clientId = StubConstants.CLIENT_ID,
            clientSecret = StubConstants.CLIENT_SECRET,
            validApiKey = StubConstants.API_KEY,
        )

    override suspend fun startAuthFlow(id: String, secret: String, uri: String, scopes: String): String =
        StubConstants.TOKEN

    override suspend fun verifyCredentials(): ApplicationDetails =
        ApplicationDetails(
            name = StubConstants.NAME,
            website = StubConstants.WEBSITE,
            clientId = StubConstants.CLIENT_ID,
            clientSecret = StubConstants.CLIENT_SECRET,
            validApiKey = StubConstants.API_KEY,
        )
}
