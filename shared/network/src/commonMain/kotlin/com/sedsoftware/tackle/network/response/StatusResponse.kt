package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class StatusResponse(
    @SerialName("id") val id: String,
    @SerialName("uri") val uri: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("account") val account: AccountResponse,
    @SerialName("content") val content: String,
    @SerialName("visibility") val visibility: String = "",
    @SerialName("sensitive") val sensitive: Boolean,
    @SerialName("spoiler_text") val spoilerText: String,
    @SerialName("media_attachments") val mediaAttachments: List<MediaAttachmentResponse> = emptyList(),
    @SerialName("application") val application: ApplicationResponse? = null,
    @SerialName("mentions") val mentions: List<StatusMentionResponse> = emptyList(),
    @SerialName("tags") val tags: List<StatusTagResponse> = emptyList(),
    @SerialName("emojis") val emojis: List<CustomEmojiResponse> = emptyList(),
    @SerialName("reblogs_count") val reblogsCount: Long,
    @SerialName("favourites_count") val favouritesCount: Long,
    @SerialName("replies_count") val repliesCount: Long,
    @SerialName("url") val url: String = "",
    @SerialName("in_reply_to_id") val inReplyToId: String = "",
    @SerialName("in_reply_to_account_id") val inReplyToAccountId: String = "",
    @SerialName("reblog") val reblog: StatusResponse? = null,
    @SerialName("poll") val poll: PollResponse? = null,
    @SerialName("card") val card: PreviewCardResponse? = null,
    @SerialName("language") val language: String = "",
    @SerialName("text") val text: String = "",
    @SerialName("edited_at") val editedAt: String = "",
    @SerialName("favourited") val favourited: Boolean = false,
    @SerialName("reblogged") val reblogged: Boolean = false,
    @SerialName("muted") val muted: Boolean = false,
    @SerialName("bookmarked") val bookmarked: Boolean = false,
    @SerialName("pinned") val pinned: Boolean = false,
    @SerialName("filtered") val filtered: List<FilterResultResponse> = emptyList(),
)

@Serializable
internal class StatusMentionResponse(
    @SerialName("id") val id: String = "",
    @SerialName("username") val username: String = "",
    @SerialName("url") val url: String = "",
    @SerialName("acct") val acct: String = "",
)

@Serializable
internal class StatusTagResponse(
    @SerialName("name") val name: String = "",
    @SerialName("url") val url: String = "",
)

@Serializable
internal class ScheduledStatusResponse(
    @SerialName("id") val id: String,
    @SerialName("scheduled_at") val scheduledAt: String,
    @SerialName("params") val params: ScheduledStatusParamsResponse,
    @SerialName("media_attachments") val mediaAttachments: List<MediaAttachmentResponse> = emptyList(),
)

@Serializable
internal class ScheduledStatusPollResponse(
    @SerialName("multiple") val multiple: Boolean = false,
    @SerialName("hide_totals") val hideTotals: Boolean = false,
    @SerialName("expires_in") val expiresIn: Long = 0L,
    @SerialName("options") val options: List<String> = emptyList(),
)

@Serializable
internal class ScheduledStatusParamsResponse(
    @SerialName("text") val text: String,
    @SerialName("poll") val poll: ScheduledStatusPollResponse? = null,
    @SerialName("media_ids") val mediaIds: List<String> = emptyList(),
    @SerialName("sensitive") val sensitive: Boolean = false,
    @SerialName("spoiler_text") val spoilerText: String = "",
    @SerialName("visibility") val visibility: String = "",
    @SerialName("in_reply_to_id") val inReplyToId: Long = 0L,
    @SerialName("language") val language: String = "",
    @SerialName("application_id") val applicationId: Int,
    @SerialName("idempotency") val idempotency: String = "",
    @SerialName("with_rate_limit") val withRateLimit: Boolean = false,
)
