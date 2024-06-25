package com.sedsoftware.tackle.network.response

import com.sedsoftware.tackle.network.response.type.PreviewCardTypeRemote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class PreviewCardResponse(
    @SerialName("url") val url: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("type") val type: PreviewCardTypeRemote = PreviewCardTypeRemote.UNKNOWN,
    @SerialName("author_name") val authorName: String,
    @SerialName("author_url") val authorUrl: String,
    @SerialName("provider_name") val providerName: String,
    @SerialName("provider_url") val providerUrl: String,
    @SerialName("html") val html: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("image") val image: String = "",
    @SerialName("embed_url") val embedUrl: String = "",
    @SerialName("blurhash") val blurhash: String = "",
)
