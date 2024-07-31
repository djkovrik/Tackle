package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class TagResponse(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
    @SerialName("history") val history: List<TagHistoryResponse>,
    @SerialName("following") val following: Boolean = false,
    @SerialName("trendable") val trendable: Boolean = true,
    @SerialName("usable") val usable: Boolean = true,
    @SerialName("requires_review") val requiresReview: Boolean = false,
)

@Serializable
internal class TagHistoryResponse(
    @SerialName("day") val day: String,
    @SerialName("uses") val uses: String,
    @SerialName("accounts") val accounts: String,
)
