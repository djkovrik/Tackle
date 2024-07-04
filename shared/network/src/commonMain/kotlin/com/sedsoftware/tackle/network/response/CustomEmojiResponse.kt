package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class CustomEmojiResponse(
    @SerialName("shortcode") val shortcode: String,
    @SerialName("url") val url: String,
    @SerialName("static_url") val staticUrl: String,
    @SerialName("visible_in_picker") val visibleInPicker: Boolean,
    @SerialName("category") val category: String = "",
)
