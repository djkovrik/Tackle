package com.sedsoftware.tackle.database.mappers

import com.sedsoftware.tackle.database.ServerEmojiEntity
import com.sedsoftware.tackle.domain.model.CustomEmoji

internal object ServerEmojiListMapper {

    fun map(from: List<ServerEmojiEntity>): List<CustomEmoji> = from.map(ServerEmojiItemMapper::map)
}
