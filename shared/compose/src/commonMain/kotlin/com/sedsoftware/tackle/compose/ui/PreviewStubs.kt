package com.sedsoftware.tackle.compose.ui

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.StatusMention
import com.sedsoftware.tackle.domain.model.StatusTag
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.domain.model.type.ShortDateUnit
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.utils.extension.toLocalDateCustom
import com.sedsoftware.tackle.utils.extension.toLocalDateTimeCustom
import kotlinx.datetime.LocalDateTime

internal object PreviewStubs {
    private val accountStub: Account = Account(
        id = "12345",
        username = "djkovrik",
        acct = "djkovrik",
        displayName = "Somebody used to know",
        note = "123",
        url = "https://mastodon.social/@tackle",
        uri = "https://mastodon.social/users/tackle",
        avatar = "https://mastodon.social/avatars/original/missing.png",
        avatarStatic = "https://mastodon.social/avatars/original/missing.png",
        header = "https://mastodon.social/headers/original/missing.png",
        headerStatic = "https://mastodon.social/headers/original/missing.png",
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

    private val statusBase: Status = Status(
        id = "12345",
        uri = "https://mastodon.social/users/tackle/statuses/11303351005096066",
        url = "https://mastodon.social/@tackle/113033510050960664",
        createdAt = LocalDateTime.parse("2023-01-02T23:40:57.120"),
        createdAtShort = ShortDateUnit.Days(3),
        createdAtPretty = "02.01.2023 23:40",
        editedAt = LocalDateTime.parse("2023-01-02T23:44:57.120"),
        editedAtShort = ShortDateUnit.Hours(2),
        editedAtPretty = "02.01.2023 23:49",
        account = accountStub,
        content = "This is some test status content.",
        contentAsPlainText = "This is some test status content.",
        visibility = StatusVisibility.PUBLIC,
        sensitive = false,
        spoilerText = "",
        mediaAttachments = emptyList(),
        application = Application(
            name = "Tackle",
            website = "",
            clientId = "",
            clientSecret = "",
        ),
        mentions = emptyList(),
        tags = emptyList(),
        emojis = emptyList(),
        reblogsCount = 1,
        favouritesCount = 21,
        repliesCount = 32,
        inReplyToId = "",
        inReplyToAccountId = "",
        reblog = null,
        poll = null,
        language = "en",
        text = "Text",
        favourited = false,
        reblogged = false,
        muted = false,
        bookmarked = false,
        pinned = false,
        filtered = emptyList(),
        card = null,
    )

    val statusNormal: Status = statusBase

    val statusWithLongNames: Status = statusBase.copy(
        account = accountStub.copy(
            displayName = "Very long name name name name name name name name name",
            acct = "act act act act act act act act act act act act act act act act act act"
        ),
        editedAtPretty = null,
    )

    val statusWithEmbeddedContent: Status = statusBase.copy(
        text = "",
        contentAsPlainText = "Status with emoji :blobcat: and hashtag #protip and mention @djkovrik :lol: :kek:" +
                " Let also add a link like https://google.com here and no schema google.com as well.",
        mentions = listOf(
            StatusMention(
                id = "1111",
                username = "djkovrik",
                url = "https://mastodon.social/@djkovrik",
                acct = "djkovrik",
            )
        ),
        tags = listOf(
            StatusTag(
                name = "protip",
                url = "https://mastodon.social/tags/protip",
            )
        ),
        emojis = listOf(
            CustomEmoji(
                shortcode = "blobcat",
                url = "https://files.mastodon.social/custom_emojis/images/000/023/743/original/f31b4b0111ad8b08.png",
                staticUrl = "https://files.mastodon.social/custom_emojis/images/000/023/743/static/f31b4b0111ad8b08.png",
                visibleInPicker = true,
                category = ""
            ),
            CustomEmoji(
                shortcode = "lol",
                url = "https://files.mastodon.social/custom_emojis/images/000/023/743/original/f31b4b0111ad8b08.png",
                staticUrl = "https://files.mastodon.social/custom_emojis/images/000/023/743/static/f31b4b0111ad8b08.png",
                visibleInPicker = true,
                category = ""
            ),
            CustomEmoji(
                shortcode = "kek",
                url = "https://files.mastodon.social/custom_emojis/images/000/023/743/original/f31b4b0111ad8b08.png",
                staticUrl = "https://files.mastodon.social/custom_emojis/images/000/023/743/static/f31b4b0111ad8b08.png",
                visibleInPicker = true,
                category = ""
            ),
        )
    )

    val mediaAttachment: MediaAttachment = MediaAttachment(
        id = "id",
        type = MediaAttachmentType.UNKNOWN,
        url = "url",
        previewUrl = "previewUrl",
        remoteUrl = "remoteUrl",
        description = "description",
        blurhash = "blurhash",
        meta = null,
    )
}
