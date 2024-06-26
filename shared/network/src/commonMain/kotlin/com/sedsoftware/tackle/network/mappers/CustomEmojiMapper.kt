package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.network.model.CustomEmoji
import com.sedsoftware.tackle.network.response.CustomEmojiResponse

internal object CustomEmojiMapper {

    fun map(from: List<CustomEmojiResponse>): List<CustomEmoji> = from.map(::mapItem)

    private fun mapItem(from: CustomEmojiResponse): CustomEmoji =
        CustomEmoji(
            shortcode = from.shortcode,
            url = from.url,
            staticUrl = from.staticUrl,
            visibleInPicker = from.visibleInPicker,
            category = from.category,
        )
}
