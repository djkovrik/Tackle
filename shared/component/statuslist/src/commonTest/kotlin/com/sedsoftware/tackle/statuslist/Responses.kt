package com.sedsoftware.tackle.statuslist

import com.sedsoftware.tackle.domain.model.Poll
import com.sedsoftware.tackle.domain.model.PollOption
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.ShortDateUnit
import com.sedsoftware.tackle.domain.model.type.StatusVisibility.PUBLIC
import kotlinx.datetime.LocalDateTime

internal object Responses {
    val status: Status = Status(
        id = "id",
        uri = "uri",
        createdAt = LocalDateTime.parse("2023-01-02T23:40:57.120"),
        createdAtShort = ShortDateUnit.Now,
        createdAtPretty = "02.01.2023 23:40",
        editedAt = LocalDateTime.parse("2023-01-02T23:40:57.120"),
        editedAtShort = ShortDateUnit.Now,
        editedAtPretty = "02.01.2023 23:40",
        account = Instances.account,
        content = "Test content",
        contentAsPlainText = "Test content",
        visibility = PUBLIC,
        sensitive = false,
        spoilerText = "",
        mediaAttachments = emptyList(),
        application = null,
        mentions = emptyList(),
        tags = emptyList(),
        emojis = emptyList(),
        reblogsCount = 1,
        favouritesCount = 2,
        repliesCount = 3,
        url = "url",
        inReplyToId = "",
        inReplyToAccountId = "",
        reblog = null,
        poll = Poll(
            id = "123",
            expiresAt = LocalDateTime.parse("2025-01-02T23:44:57.120"),
            expiresAtStr = "02.01.2025 23:44",
            expired = false,
            multiple = true,
            votesCount = 100,
            votersCount = 100,
            options = listOf(
                PollOption(title = "Option 1", 17),
                PollOption(title = "Option 2", 54),
                PollOption(title = "Option 3", 29),
            ),
            emojis = emptyList(),
            voted = false,
            hideTotals = false,
            ownVotes = emptyList(),
        ),
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
}
