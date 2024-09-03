package com.sedsoftware.tackle.editor.stubs

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.utils.extension.toLocalDate
import com.sedsoftware.tackle.utils.extension.toLocalDateTime

object AccountStub {
    val accountDetails = Account(
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
}
