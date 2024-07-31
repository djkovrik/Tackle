package com.sedsoftware.tackle.database.mappers

import com.sedsoftware.tackle.database.ServerEmojiEntity
import com.sedsoftware.tackle.domain.model.CustomEmoji

internal object ServerEmojiItemMapper {

    fun map(from: ServerEmojiEntity): CustomEmoji =
        CustomEmoji(
            shortcode = from.shortcode,
            url = from.url,
            staticUrl = from.staticUrl,
            visibleInPicker = from.visibleInPicker == 1L,
            category = from.category,
        )
}
