package com.sedsoftware.tackle.editor.domain

import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.model.EditorProfileData
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.utils.toLocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class StatusEditorManager(
    private val api: EditorTabComponentGateways.Api,
    private val database: EditorTabComponentGateways.Database,
    private val settings: EditorTabComponentGateways.Settings,
    private val tools: EditorTabComponentGateways.Tools,
) {

    fun getEditorProfileData(): Result<EditorProfileData> = runCatching {
        return@runCatching EditorProfileData(
            avatar = settings.ownAvatar,
            name = settings.ownNickname,
        )
    }

    fun getCurrentLocale(): Result<AppLocale> = runCatching {
        return@runCatching tools.getCurrentLocale()
    }

    fun getAvailableLocales(): Result<List<AppLocale>> = runCatching {
        return@runCatching tools.getAvailableLocales()
    }

    suspend fun fetchServerEmojis(): Result<Boolean> = runCatching {
        // sync once per day
        val lastSync: LocalDate = settings.emojiLastCachedTimestamp.toLocalDate()
        if (isSameDay(lastSync, today())) {
            return@runCatching true
        }

        val response: List<CustomEmoji> = api.getServerEmojis()
        database.cacheServerEmojis(response)

        settings.emojiLastCachedTimestamp = today().toString()

        return@runCatching response.isNotEmpty()
    }

    suspend fun observeCachedEmojis(): Flow<List<CustomEmoji>> =
        database.observeCachedEmojis()

    private fun isSameDay(first: LocalDate, second: LocalDate): Boolean =
        first.year == second.year && first.dayOfYear == second.dayOfYear

    private fun today(): LocalDate =
        Clock.System.now().toLocalDateTime(timeZone = TimeZone.currentSystemDefault()).date
}
