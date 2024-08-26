package com.sedsoftware.tackle.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class CreateStatusRequest(
    @SerialName("status") val status: String,
    @SerialName("media_ids") val mediaIds: List<String>?,
    @SerialName("poll") val poll: CreateStatusRequestPoll?,
    @SerialName("in_reply_to_id") val inReplyToId: String?,
    @SerialName("sensitive") val sensitive: Boolean?,
    @SerialName("spoiler_text") val spoilerText: String?,
    @SerialName("visibility") val visibility: String?,
    @SerialName("language") val language: String?,
    @SerialName("scheduled_at") val scheduledAt: String?,
)

@Serializable
internal class CreateStatusRequestPoll(
    @SerialName("options") val options: List<String>,
    @SerialName("expires_in") val expiresIn: Long,
    @SerialName("multiple") val multiple: Boolean,
    @SerialName("hide_totals") val hideTotals: Boolean,
)
