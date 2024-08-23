package com.sedsoftware.tackle.editor.stubs

import com.sedsoftware.tackle.domain.model.ScheduledStatus
import com.sedsoftware.tackle.domain.model.ScheduledStatusParams
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import kotlinx.datetime.LocalDateTime

internal object StatusStub {
    val normal: Status = Status(
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

    val scheduled: ScheduledStatus = ScheduledStatus(
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
            mediaAttachments = emptyList(),
        )
    )
}
