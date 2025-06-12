package com.sedsoftware.tackle.auth

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.AppClientData
import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.utils.extension.toLocalDateCustom
import com.sedsoftware.tackle.utils.extension.toLocalDateTimeCustom

internal object Responses {

    const val TOKEN = "Token"

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
            id = Constants.OWN_USER_ID,
            username = Constants.OWN_USERNAME,
            acct = Constants.OWN_USERNAME,
            displayName = Constants.OWN_USERNAME,
            note = "123",
            url = "url",
            uri = "uri",
            avatar = Constants.OWN_AVATAR,
            avatarStatic = Constants.OWN_AVATAR,
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
            createdAt = "2023-01-02T23:40:57.12".toLocalDateTimeCustom(),
            lastStatusAt = "2023-01-02".toLocalDateCustom(),
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
            createdAt = "2023-01-02T23:40:57.12".toLocalDateTimeCustom(),
            lastStatusAt = "2023-01-02".toLocalDateCustom(),
            statusesCount = 123L,
            followersCount = 321L,
            followingCount = 1234L,
            moved = null,
            source = null,
            role = null,
        )

    val validClientData: AppClientData = AppClientData(
        name = Constants.NAME,
        uri = Constants.URI,
        scopes = Constants.SCOPES,
        website = Constants.WEBSITE
    )
}
