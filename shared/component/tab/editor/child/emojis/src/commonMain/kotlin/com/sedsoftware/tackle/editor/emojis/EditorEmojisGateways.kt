package com.sedsoftware.tackle.editor.emojis

import com.sedsoftware.tackle.domain.model.CustomEmoji
import kotlinx.coroutines.flow.Flow

interface EditorEmojisGateways {
    interface Api {
        suspend fun getServerEmojis(): List<CustomEmoji>
    }

    interface Database {
        suspend fun insertEmojis(list: List<CustomEmoji>)
        suspend fun observeEmojis(): Flow<List<CustomEmoji>>
    }

    interface Settings {
        var emojiLastCachedTimestamp: String
    }
}
