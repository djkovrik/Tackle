package com.sedsoftware.tackle.auth.stubs

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.AppClientData
import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.utils.extension.toLocalDate
import com.sedsoftware.tackle.utils.extension.toLocalDateTime

class AuthComponentApiStub : AuthComponentGateways.Api {

    companion object {
        val validApplicationDetails = Application(
            name = StubConstants.NAME,
            website = StubConstants.WEBSITE,
            clientId = StubConstants.CLIENT_ID,
            clientSecret = StubConstants.CLIENT_SECRET,
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
            rules = emptyList(),
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
            rules = emptyList(),
        )

        val validAccountDetails = Account(
            id = "12345",
            username = "djkovrik",
            acct = "djkovrik",
            displayName = "djkovrik",
            note = "123",
            avatar = "url",
            avatarStatic = "url",
            header = "url",
            headerStatic = "url",
            locked = false,
            fields = emptyList(),
            emojis = emptyList(),
            bot = false,
            group = false,
            discoverable = false,
            noIndex = false,
            suspended = false,
            limited = false,
            createdAt = "2023-01-02T23:40:57.12".toLocalDateTime(),
            lastStatusAt = "2023-01-02".toLocalDate(),
            statusesCount = 123L,
            followersCount = 321L,
            followingCount = 1234L,
            moved = null,
            source = null,
            role = null,
        )

        val invalidAccountDetails = Account(
            id = "",
            username = "",
            acct = "",
            displayName = "",
            note = "",
            avatar = "",
            avatarStatic = "",
            header = "",
            headerStatic = "",
            locked = false,
            fields = emptyList(),
            emojis = emptyList(),
            bot = false,
            group = false,
            discoverable = false,
            noIndex = false,
            suspended = false,
            limited = false,
            createdAt = "2023-01-02T23:40:57.12".toLocalDateTime(),
            lastStatusAt = "2023-01-02".toLocalDate(),
            statusesCount = 123L,
            followersCount = 321L,
            followingCount = 1234L,
            moved = null,
            source = null,
            role = null,
        )
    }

    var getServerInfoResponse: Instance = validInstanceDetails
    var createAppResponse: Application = validApplicationDetails
    var startAuthFlowResponse: String = StubConstants.TOKEN
    var verifyCredentialsResponse: Account = validAccountDetails
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

    override suspend fun verifyCredentials(): Account =
        if (!shouldThrowException) {
            verifyCredentialsResponse
        } else {
            error("verifyCredentials exception")
        }
}
