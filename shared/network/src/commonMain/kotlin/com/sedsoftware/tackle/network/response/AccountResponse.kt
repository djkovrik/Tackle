package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class AccountResponse(
    @SerialName("id") val id: String = "",
    @SerialName("username") val username: String = "",
    @SerialName("acct") val acct: String = "",
    @SerialName("display_name") val displayName: String = "",
    @SerialName("note") val note: String = "",
    @SerialName("avatar") val avatar: String = "",
    @SerialName("avatar_static") val avatarStatic: String = "",
    @SerialName("header") val header: String = "",
    @SerialName("header_static") val headerStatic: String = "",
    @SerialName("locked") val locked: Boolean = false,
    @SerialName("fields") val fields: List<FieldResponse> = emptyList(),
    @SerialName("emojis") val emojis: List<CustomEmojiResponse> = emptyList(),
    @SerialName("bot") val bot: Boolean = false,
    @SerialName("group") val group: Boolean = false,
    @SerialName("discoverable") val discoverable: Boolean = false,
    @SerialName("noindex") val noIndex: Boolean = false,
    @SerialName("moved ") val moved: AccountResponse? = null,
    @SerialName("suspended") val suspended: Boolean = false,
    @SerialName("limited") val limited: Boolean = false,
    @SerialName("created_at") val createdAt: String = "",
    @SerialName("last_status_at") val lastStatusAt: String = "",
    @SerialName("statuses_count") val statusesCount: Long = 0L,
    @SerialName("followers_count") val followersCount: Long = 0L,
    @SerialName("following_count") val followingCount: Long = 0L,
)

@Serializable
internal class FieldResponse(
    @SerialName("name") val name: String = "",
    @SerialName("value") val value: String = "",
    @SerialName("verified_at") val verifiedAt: String = "",
)
