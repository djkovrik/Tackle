package com.sedsoftware.tackle.statuslist

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.utils.extension.toLocalDateCustom
import com.sedsoftware.tackle.utils.extension.toLocalDateTimeCustom

internal object Instances {
    val account: Account = Account(
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
        createdAt = "2023-01-02T23:40:57.12".toLocalDateTimeCustom(),
        lastStatusAt = "2023-01-02".toLocalDateCustom(),
        statusesCount = 123L,
        followersCount = 321L,
        followingCount = 1234L,
        moved = null,
        source = null,
        role = null,
    )
}
