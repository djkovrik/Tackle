package com.sedsoftware.tackle.editor.stubs

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.HashTag
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.MediaAttachmentFocus
import com.sedsoftware.tackle.domain.model.MediaAttachmentMeta
import com.sedsoftware.tackle.domain.model.ScheduledStatus
import com.sedsoftware.tackle.domain.model.ScheduledStatusParams
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import kotlinx.datetime.LocalDateTime

object EditorComponentApiStubResponses {
    val emojis: List<CustomEmoji> = listOf(
        CustomEmoji("kek", "", "", true, "")
    )

    val searchResponseDefault: Search =
        Search(
            accounts = listOf(
                AccountStub.accountDetails.copy(id = "1", username = "name1"),
                AccountStub.accountDetails.copy(id = "2", username = "name2"),
                AccountStub.accountDetails.copy(id = "3", username = "name3"),
                AccountStub.accountDetails.copy(id = "4", username = "name4"),
            ),
            hashtags = listOf(
                HashTag("Tag1", "url1", emptyList(), following = false, trendable = false, usable = true, requiresReview = false),
                HashTag("Tag2", "url2", emptyList(), following = false, trendable = false, usable = true, requiresReview = false),
                HashTag("Tag3", "url3", emptyList(), following = false, trendable = false, usable = true, requiresReview = false),
                HashTag("Tag4", "url4", emptyList(), following = false, trendable = false, usable = true, requiresReview = false),
            ),
            statuses = emptyList(),
        )

    val statusNormal: Status =
        Status(
            id = "id",
            uri = "uri",
            createdAt = LocalDateTime.parse("2023-01-02T23:40:57.120"),
            editedAt = LocalDateTime.parse("2023-01-02T23:40:57.120"),
            account = AccountStub.accountDetails,
            content = "Test content",
            visibility = StatusVisibility.PUBLIC,
            sensitive = false,
            spoilerText = "",
            mediaAttachments = emptyList(),
            application = null,
            mentions = emptyList(),
            tags = emptyList(),
            emojis = emptyList(),
            reblogsCount = 1L,
            favouritesCount = 2L,
            repliesCount = 3L,
            url = "url",
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

    val statusScheduled: ScheduledStatus =
        ScheduledStatus(
            id = "id",
            scheduledAt = LocalDateTime.parse("2025-01-02T23:40:57.120"),
            params = ScheduledStatusParams(
                text = "Text",
                poll = null,
                mediaIds = emptyList(),
                sensitive = false,
                spoilerText = "",
                visibility = StatusVisibility.PUBLIC,
                inReplyToId = 0L,
                language = "en",
                applicationId = 123,
                idempotency = "kek",
                withRateLimit = false,
            )
        )

    fun buildAttachmentResponse(description: String?, focus: String?): MediaAttachment =
        MediaAttachment(
            id = "id",
            type = MediaAttachmentType.IMAGE,
            url = "url",
            remoteUrl = "remoteUrl",
            previewUrl = "previewUrl",
            description = description.orEmpty(),
            blurhash = "blur",
            meta = MediaAttachmentMeta(
                length = "",
                duration = 0f,
                fps = 0,
                size = "",
                width = 0,
                height = 0,
                aspect = 0f,
                audioEncode = "",
                audioBitrate = "",
                audioChannels = "",
                original = null,
                small = null,
                focus = focus?.let {
                    val x = focus.substringBefore(",").toFloat()
                    val y = focus.substringAfter(",").toFloat()
                    MediaAttachmentFocus(x, y)
                }
            ),
        )
}
