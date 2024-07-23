package com.sedsoftware.tackle.database.mappers

import com.sedsoftware.tackle.database.ServerEmojiEntity
import com.sedsoftware.tackle.domain.model.CustomEmoji

internal object ServerEmojiEntityMapper {

    fun map(from: List<ServerEmojiEntity>): Map<String, List<CustomEmoji>> =
        from.map(::mapItem).filter { it.visibleInPicker }.sortedBy { it.category.isNotBlank() }.groupBy { it.category }

    private fun mapItem(from: ServerEmojiEntity): CustomEmoji =
        CustomEmoji(
            shortcode = from.shortcode,
            url = from.url,
            staticUrl = from.staticUrl,
            visibleInPicker = from.visibleInPicker == 1L,
            category = from.category,
        )
}
