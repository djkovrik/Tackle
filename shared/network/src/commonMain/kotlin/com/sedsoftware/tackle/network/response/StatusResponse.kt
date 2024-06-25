package com.sedsoftware.tackle.network.response

import com.sedsoftware.tackle.network.model.type.StatusVisibility
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class StatusResponse(
    @SerialName("id") val id: String = "",
    @SerialName("uri") val uri: String = "",
    @SerialName("created_at") val createdAt: String = "",
    @SerialName("account") val account: AccountResponse? = null,
    @SerialName("content") val content: String = "",
    @SerialName("visibility") val visibility: StatusVisibility = StatusVisibility.UNKNOWN,
    @SerialName("sensitive") val sensitive: Boolean = false,
    @SerialName("spoiler_text") val spoilerText: String = "",
    @SerialName("media_attachments") val mediaAttachments: List<MediaAttachmentResponse> = emptyList(),
    @SerialName("application") val application: ApplicationResponse? = null,
    @SerialName("mentions") val mentions: List<StatusMentionResponse> = emptyList(),
    @SerialName("tags") val tags: List<StatusTagResponse> = emptyList(),
    @SerialName("emojis") val emojis: List<CustomEmojiResponse> = emptyList(),
    @SerialName("reblogs_count") val reblogsCount: Long = 0L,
    @SerialName("favourites_count") val favouritesCount: Long = 0L,
    @SerialName("replies_count") val repliesCount: Long = 0L,
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
