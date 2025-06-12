package com.sedsoftware.tackle.editor

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.Instance.Config.MediaAttachments
import com.sedsoftware.tackle.domain.model.Instance.Config.Polls
import com.sedsoftware.tackle.domain.model.Instance.Config.Statuses
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.MediaAttachmentFocus
import com.sedsoftware.tackle.domain.model.MediaAttachmentInfo
import com.sedsoftware.tackle.domain.model.MediaAttachmentMeta
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.utils.extension.toLocalDateCustom
import com.sedsoftware.tackle.utils.extension.toLocalDateTimeCustom

internal object Instances {

    val customEmojiList = listOf(
        CustomEmoji("test1", "test1", "test1", true, "test1"),
        CustomEmoji("test2", "test2", "test2", true, "test1"),
        CustomEmoji("test3", "test3", "test3", true, "test1"),
        CustomEmoji("abcde", "abcde", "abcde", true, "abcde"),
    )

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
        createdAt = "2023-01-02T23:40:57.12".toLocalDateTimeCustom(),
        lastStatusAt = "2023-01-02".toLocalDateCustom(),
        statusesCount = 123L,
        followersCount = 321L,
        followingCount = 1234L,
        moved = null,
        source = null,
        role = null,
    )

    val emoji: CustomEmoji = CustomEmoji("banana_kek", "", "", true, "123")

    val instanceConfig: Instance.Config = Instance.Config(
        statuses = Statuses(
            maxCharacters = 500,
            maxMediaAttachments = 4,
        ),
        mediaAttachments = MediaAttachments(
            imageSizeLimit = 123456L,
            videoSizeLimit = 123456L,
            supportedMimeTypes = listOf(
                "video/mp4",
                "image/jpeg",
                "image/png",
            )
        ),
        polls = Polls(
            maxOptions = 4,
            maxCharactersPerOption = 50,
            minExpiration = 60,
            maxExpiration = 123456789L,
        )
    )

    val instanceInfo = Instance(
        domain = "domain",
        config = instanceConfig,
    )

    val fileToAttach = MediaAttachment(
        id = "123",
        type = MediaAttachmentType.IMAGE,
        url = "url",
        previewUrl = "url",
        remoteUrl = "url",
        description = "Description",
        blurhash = "hash",
        meta = MediaAttachmentMeta(
            length = "",
            duration = 1f,
            fps = 123,
            size = "",
            width = 123,
            height = 321,
            aspect = 1f,
            audioEncode = "",
            audioBitrate = "",
            audioChannels = "",
            focus = MediaAttachmentFocus(1f, 1f),
            original = null,
            small = MediaAttachmentInfo(
                width = 123,
                height = 321,
                size = "",
                aspect = 1f,
                frameRate = "",
                duration = 1f,
                bitrate = 2L,
            ),
        ),
    )
}
