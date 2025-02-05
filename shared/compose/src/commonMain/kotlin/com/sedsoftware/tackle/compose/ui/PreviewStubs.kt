package com.sedsoftware.tackle.compose.ui

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PreviewCard
import com.sedsoftware.tackle.domain.model.PreviewCardAuthor
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.StatusMention
import com.sedsoftware.tackle.domain.model.StatusTag
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.domain.model.type.PreviewCardType
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
        blurhash = "",
        meta = null,
    )

    val previewCardImage = PreviewCard(
        url = "https://www.flickr.com/photos/tomfenskephotography/49088768431/",
        title = "Oregon",
        description = "",
        type = PreviewCardType.PHOTO,
        authors = listOf(
            PreviewCardAuthor(
                name = "Tom Fenske Photography",
                url = "https://www.flickr.com/photos/tomfenskephotography/",
                account = null,
            )
        ),
        authorName = "Tom Fenske Photography",
        authorUrl = "https://www.flickr.com/photos/tomfenskephotography/",
        providerName = "Flickr",
        providerUrl = "https://www.flickr.com/",
        html = "",
        width = 1024,
        height = 427,
        image = "https://files.mastodon.social/preview_cards/images/014/287/139/original/651b1c6976817824.jpeg",
        embedUrl = "https://live.staticflickr.com/65535/49088768431_6a4322b3bb_b.jpg",
        blurhash = "UnE{@jt6M_oIAhjYs+ayT2WBf9ayRkkDXAj[",
    )

    val previewCardVideo = PreviewCard(
        url = "https://www.youtube.com/watch?v=OMv_EPMED8Y",
        title = "♪ Brand New Friend (Christmas Song!)",
        description = "",
        type = PreviewCardType.VIDEO,
        authors = listOf(
            PreviewCardAuthor(
                name = "YOGSCAST Lewis & Simon",
                url = "https://www.youtube.com/user/BlueXephos",
                account = null,
            )
        ),
        authorName = "YOGSCAST Lewis & Simon",
        authorUrl = "https://www.youtube.com/user/BlueXephos",
        providerName = "YouTube",
        providerUrl = "https://www.youtube.com/",
        html = "",
        width = 480,
        height = 270,
        image = "https://files.mastodon.social/preview_cards/images/014/179/145/original/9cf4b7cf5567b569.jpeg",
        embedUrl = "",
        blurhash = "UvK0HNkV,:s9xBR%njog0fo2W=WBS5ozofV@",
    )

    val previewCardLink = PreviewCard(
        url = "https://www.nbcnews.com/specials/plan-your-vote-2022-elections/index.html",
        title = "Plan Your Vote: 2022 Elections",
        description = "Everything you need to know about the voting rules where you live, including registration, mail-in voting, changes since 2020, and more.",
        type = PreviewCardType.LINK,
        authors = listOf(
            PreviewCardAuthor(
                name = "NBC News",
                url = "",
                account = null,
            )
        ),
        authorName = "NBC News",
        authorUrl = "",
        providerName = "NBC News",
        providerUrl = "",
        html = "",
        width = 400,
        height = 225,
        image = "https://files.mastodon.social/cache/preview_cards/images/045/027/478/original/0783d5e91a14fd49.jpeg",
        embedUrl = "",
        blurhash = "UcQmF#ay~qofj[WBj[j[~qof9Fayofofayay",
    )
}
