package com.sedsoftware.tackle.editor

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.AppLocale
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
        val domain: String
        var emojiLastCachedTimestamp: String
        var lastSelectedLanguageName: String
        var lastSelectedLanguageCode: String
    }

    interface Tools {
        fun getCurrentLocale(): AppLocale
        fun getAvailableLocales(): List<AppLocale>
    }
}
