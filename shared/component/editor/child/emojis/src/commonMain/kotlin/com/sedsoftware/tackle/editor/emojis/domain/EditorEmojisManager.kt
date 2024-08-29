package com.sedsoftware.tackle.editor.emojis.domain

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.emojis.EditorEmojisGateways
import com.sedsoftware.tackle.utils.extension.toLocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class EditorEmojisManager(
    private val api: EditorEmojisGateways.Api,
    private val database: EditorEmojisGateways.Database,
    private val settings: EditorEmojisGateways.Settings,
    private val today: LocalDate = today(),
) {

    suspend fun fetchServerEmojis(): Result<Unit> = runCatching {
        val lastSyncDate: LocalDate = settings.emojiLastCachedTimestamp.toLocalDate()

        if (isDateNotToday(lastSyncDate)) {
            val response: List<CustomEmoji> = api.getServerEmojis()
            database.insertEmojis(response)
            settings.emojiLastCachedTimestamp = today.toString()
        }
    }

    suspend fun observeCachedEmojis(): Flow<Map<String, List<CustomEmoji>>> {
        return database.observeEmojis()
    }

    private fun isDateNotToday(first: LocalDate): Boolean =
        first.year != today.year || first.dayOfYear != today.dayOfYear

    private companion object {
        fun today(): LocalDate = Clock.System.now().toLocalDateTime(timeZone = TimeZone.currentSystemDefault()).date
    }
}
