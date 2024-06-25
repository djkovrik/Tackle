package com.sedsoftware.tackle.network.model

import com.sedsoftware.tackle.network.model.type.StatusVisibility
import kotlinx.datetime.LocalDateTime

data class Status(
    val id: String,
    val uri: String,
    val createdAt: LocalDateTime,
    val account: Account,
    val content: String,
    val visibility: StatusVisibility,
    val sensitive: Boolean,
    val spoilerText: String,
    val mediaAttachments: List<MediaAttachment>,
    val application: Application?,
    val mentions: List<StatusMention>,
    val tags: List<StatusTag>,
    val emojis: List<CustomEmoji>,
    val reblogsCount: Long,
    val favouritesCount: Long,
    val repliesCount: Long,
    val url: String,
    val inReplyToId: String,
    val inReplyToAccountId: String,
    val reblog: Status?,
    val poll: Poll?,
    val card: PreviewCard?,
    val language: String,
    val text: String,
    val editedAt: LocalDateTime,
    val favourited: Boolean,
    val reblogged: Boolean,
    val muted: Boolean,
    val bookmarked: Boolean,
    val pinned: Boolean,
    val filtered: List<FilterResult>,
)

data class StatusMention(
    val id: String,
    val username: String,
    val url: String,
    val acct: String,
)

data class StatusTag(
    val name: String,
    val url: String,
)

data class ScheduledStatus(
    val id: String,
    val scheduledAt: LocalDateTime,
    val params: ScheduledStatusParams,
)

data class ScheduledStatusParams(
    val text: String,
    val poll: Poll? = null,
    val mediaIds: List<String> = emptyList(),
    val sensitive: Boolean = false,
    val spoilerText: String = "",
    val visibility: StatusVisibility = StatusVisibility.UNKNOWN,
    val inReplyToId: Long = 0L,
    val language: String = "",
    val applicationId: Int,
    val idempotency: String = "",
    val withRateLimit: Boolean = false,
    val mediaAttachments: List<MediaAttachment> = emptyList(),
)
