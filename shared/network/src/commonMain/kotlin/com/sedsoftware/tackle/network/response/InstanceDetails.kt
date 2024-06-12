package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class InstanceDetails(
    @SerialName("domain") val domain: String,
    @SerialName("title") val title: String,
    @SerialName("version") val version: String,
    @SerialName("source_url") val sourceUrl: String,
    @SerialName("description") val description: String,
    @SerialName("usage") val usage: InstanceUsers,
    @SerialName("thumbnail") val thumbnail: InstanceThumbnail,
    @SerialName("languages") val languages: List<String>,
)

@Serializable
class InstanceUsers(
    @SerialName("users") val users: InstanceActiveMonth,
)

@Serializable
class InstanceActiveMonth(
    @SerialName("active_month") val activePerMonth: Long,
)

@Serializable
class InstanceThumbnail(
    @SerialName("url") val url: String,
    @SerialName("blurhash") val blurHash: String,
    @SerialName("versions") val versions: Map<String, String>,
)
