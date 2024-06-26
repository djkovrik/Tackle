package com.sedsoftware.tackle.database.internal

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import com.sedsoftware.tackle.database.TackleAppDatabase
import com.sedsoftware.tackle.database.TackleAppDatabaseQueries
import com.sedsoftware.tackle.database.mappers.ServerEmojiEntityMapper
import com.sedsoftware.tackle.domain.TackleDatabase
import com.sedsoftware.tackle.domain.model.CustomEmoji
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

internal class TackleSharedDatabase(
    private val currentDomainProvider: () -> String,
    private val coroutineContext: CoroutineContext,
    private val driver: SqlDriver
) : TackleDatabase {

    private val currentDomain: String
        get() = currentDomainProvider.invoke()

    private val database: TackleAppDatabase =
        TackleAppDatabase(driver)

    private val queries: TackleAppDatabaseQueries
        get() = database.tackleAppDatabaseQueries

    override suspend fun insertEmojis(list: List<CustomEmoji>) {
        queries.transaction {
            list.forEach { emojiItem ->
                val key = emojiItem.shortcode + currentDomain
                queries.insertEmoji(
                    key = key,
                    domain = currentDomain,
                    shortcode = emojiItem.shortcode,
                    url = emojiItem.url,
                    staticUrl = emojiItem.staticUrl,
                    visibleInPicker = if (emojiItem.visibleInPicker) 1L else 0L,
                    category = emojiItem.category
                )
            }
        }
    }

    override suspend fun observeEmojis(): Flow<List<CustomEmoji>> =
        queries.selectEmojis(currentDomain)
            .asFlow()
            .mapToList(coroutineContext)
            .map(ServerEmojiEntityMapper::map)
}
