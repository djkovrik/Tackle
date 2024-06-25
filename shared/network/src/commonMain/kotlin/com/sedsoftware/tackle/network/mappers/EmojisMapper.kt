package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.network.model.CustomEmoji
import com.sedsoftware.tackle.network.response.CustomEmojiResponse

internal object EmojisMapper {

    fun map(from: CustomEmojiResponse): CustomEmoji =
        CustomEmoji(
            shortcode = from.shortcode,
            url = from.url,
            staticUrl = from.staticUrl,
            visibleInPicker = from.visibleInPicker,
            category = from.category,
        )


}
