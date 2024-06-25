package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class InstanceResponse(
    @SerialName("domain") val domain: String,
    @SerialName("title") val title: String,
    @SerialName("version") val version: String,
    @SerialName("source_url") val sourceUrl: String,
    @SerialName("description") val description: String,
    @SerialName("usage") val usage: InstanceUsersResponse? = null,
    @SerialName("thumbnail") val thumbnail: InstanceThumbnailResponse? = null,
    @SerialName("languages") val languages: List<String> = emptyList(),
    @SerialName("rules") val rules: List<RuleItemResponse> = emptyList(),
)

@Serializable
internal class InstanceUsersResponse(
    @SerialName("users") val users: InstanceActiveMonthResponse,
)

@Serializable
internal class InstanceActiveMonthResponse(
    @SerialName("active_month") val activePerMonth: Long,
)

@Serializable
internal class InstanceThumbnailResponse(
    @SerialName("url") val url: String,
    @SerialName("blurhash") val blurhash: String? = null,
    @SerialName("versions") val versions: Map<String, String> = emptyMap(),
)

@Serializable
internal class RuleItemResponse(
    @SerialName("id") val id: String,
    @SerialName("text") val text: String,
    @SerialName("hint") val hint: String = "",
)
