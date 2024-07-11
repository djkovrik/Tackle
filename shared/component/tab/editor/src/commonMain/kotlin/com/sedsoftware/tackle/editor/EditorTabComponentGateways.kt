package com.sedsoftware.tackle.editor

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import kotlinx.coroutines.flow.Flow

interface EditorTabComponentGateways {
    interface Api {
        suspend fun getServerEmojis(): List<CustomEmoji>

        suspend fun sendFile(
            file: PlatformFileWrapper,
            onUpload : (Int) -> Unit = {},
            thumbnail: PlatformFileWrapper? = null,
            description: String? = null,
            focus: String? = null
        ): MediaAttachment
    }

    interface Database {
        suspend fun cacheServerEmojis(list: List<CustomEmoji>)
        suspend fun observeCachedEmojis(): Flow<List<CustomEmoji>>
        suspend fun getCachedInstanceInfo(): Flow<Instance>
    }

    interface Settings {
        val ownAvatar: String
        val ownNickname: String
        val domainShort: String
        var emojiLastCachedTimestamp: String
        var lastSelectedLanguageName: String
        var lastSelectedLanguageCode: String
    }

    interface Tools {
        fun getCurrentLocale(): AppLocale
        fun getAvailableLocales(): List<AppLocale>
    }
}
