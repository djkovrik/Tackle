package com.sedsoftware.tackle.auth.stubs

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.utils.extension.toLocalDate
import com.sedsoftware.tackle.utils.extension.toLocalDateTime

object AuthComponentApiStubResponses {

    internal object Constants {
        const val NAME = "Tackle"
        const val DOMAIN = "https://mastodon.social"
        const val URI = "tackle://sedsoftware.com"
        const val SCOPES = "read write push"
        const val WEBSITE = "https://sedsoftware.com"
        const val CLIENT_ID = "client_id"
        const val CLIENT_SECRET = "client_secret"
        const val TOKEN = "token"
    }

    val validApplicationDetails: Application =
        Application(
            name = Constants.NAME,
            website = Constants.WEBSITE,
            clientId = Constants.CLIENT_ID,
            clientSecret = Constants.CLIENT_SECRET,
        )

    val validInstanceDetails: Instance =
        Instance(
            domain = Constants.DOMAIN,
            title = Constants.DOMAIN,
            version = "1.0",
            sourceUrl = Constants.DOMAIN,
            description = Constants.NAME,
            activePerMonth = 123L,
            thumbnailUrl = Constants.DOMAIN,
            languages = listOf("en", "ru"),
            rules = emptyList(),
        )

    val invalidInstanceDetails: Instance =
        Instance(
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

    val validAccountDetails: Account =
        Account(
            id = "12345",
            username = "djkovrik",
            acct = "djkovrik",
            displayName = "djkovrik",
            note = "123",
            url = "url",
            uri = "uri",
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

    val invalidAccountDetails: Account =
        Account(
            id = "",
            username = "",
            acct = "",
            displayName = "",
            note = "",
            url = "",
            uri = "",
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
