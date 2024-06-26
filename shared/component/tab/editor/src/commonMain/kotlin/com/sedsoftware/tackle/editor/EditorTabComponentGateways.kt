package com.sedsoftware.tackle.editor

import com.sedsoftware.tackle.network.model.CustomEmoji
import com.sedsoftware.tackle.utils.model.AppLocale
import kotlinx.coroutines.flow.Flow

interface EditorTabComponentGateways {
    interface Api {
        suspend fun getServerEmojis(): List<CustomEmoji>
    }

    interface Database {
        suspend fun cacheServerEmojis(list: List<CustomEmoji>)
        suspend fun observeCachedEmojis(): Flow<List<CustomEmoji>>
    }

    interface Settings {
        val ownAvatar: String
        val ownNickname: String
        var emojiLastCachedTimestamp: String
    }

    interface Tools {
        fun getCurrentLocale(): AppLocale
        fun getAvailableLocales(): List<AppLocale>
    }
}
