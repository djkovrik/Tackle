package com.sedsoftware.tackle.editor

import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.NewStatusBundle
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.ScheduledStatus
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.domain.model.Status
import kotlinx.coroutines.flow.Flow

interface EditorTabComponentGateways {
    interface Api {
        suspend fun getServerEmojis(): List<CustomEmoji>

        suspend fun sendFile(
            file: PlatformFileWrapper,
            onUpload: (Float) -> Unit = {},
            thumbnail: PlatformFileWrapper? = null,
            description: String? = null,
            focus: String? = null,
        ): MediaAttachment

        suspend fun search(
            query: String,
            type: String,
            resolve: Boolean? = null,
            following: Boolean? = null,
            accountId: String? = null,
            excludeUnreviewed: Boolean? = null,
            minId: String? = null,
            maxId: String? = null,
            limit: Int? = null,
            offset: Int? = null,
        ): Search

        suspend fun sendStatus(bundle: NewStatusBundle): Status

        suspend fun sendStatusScheduled(bundle: NewStatusBundle): ScheduledStatus
    }

    interface Database {
        suspend fun cacheServerEmojis(list: List<CustomEmoji>)
        suspend fun observeCachedEmojis(): Flow<Map<String, List<CustomEmoji>>>
        suspend fun findEmojis(query: String): Flow<List<CustomEmoji>>
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
        fun getInputHintDelay(): Long
    }
}
