package com.sedsoftware.tackle.database.mappers

import com.sedsoftware.tackle.database.ServerEmojiEntity
import com.sedsoftware.tackle.domain.model.CustomEmoji

internal object ServerEmojiCategorizedMapper {

    fun map(from: List<ServerEmojiEntity>): Map<String, List<CustomEmoji>> =
        from.map(ServerEmojiItemMapper::map).filter { it.visibleInPicker }.sortedBy { it.category.isNotBlank() }.groupBy { it.category }
}
