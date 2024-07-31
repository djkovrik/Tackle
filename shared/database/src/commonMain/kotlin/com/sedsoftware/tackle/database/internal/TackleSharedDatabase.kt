package com.sedsoftware.tackle.database.internal

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.db.SqlDriver
import com.sedsoftware.tackle.database.InstanceInfoEntity
import com.sedsoftware.tackle.database.TackleAppDatabase
import com.sedsoftware.tackle.database.TackleAppDatabaseQueries
import com.sedsoftware.tackle.database.mappers.InstanceInfoEntityMapper
import com.sedsoftware.tackle.database.mappers.ServerEmojiCategorizedMapper
import com.sedsoftware.tackle.database.mappers.ServerEmojiListMapper
import com.sedsoftware.tackle.domain.api.TackleDatabase
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

internal class TackleSharedDatabase(
    private val currentDomainProvider: () -> String,
    private val coroutineContext: CoroutineContext,
    private val driver: SqlDriver,
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

    override suspend fun observeEmojis(): Flow<Map<String, List<CustomEmoji>>> =
        queries.selectEmojis(currentDomain)
            .asFlow()
            .mapToList(coroutineContext)
            .map(ServerEmojiCategorizedMapper::map)

    override suspend fun findEmoji(query: String): Flow<List<CustomEmoji>> =
        queries.selectEmojis(currentDomain)
            .asFlow()
            .mapToList(coroutineContext)
            .map(ServerEmojiListMapper::map)
            .map { list -> list.filter { it.shortcode.contains(query) } }

    override suspend fun cacheInstanceInfo(info: Instance) {
        val entity: InstanceInfoEntity = InstanceInfoEntityMapper.toEntity(info)
        queries.insertInstanceInfo(
            domain = entity.domain,
            title = entity.title,
            version = entity.version,
            sourceUrl = entity.sourceUrl,
            description = entity.description,
            activePerMonth = entity.activePerMonth,
            thumbnailUrl = entity.thumbnailUrl,
            blurhash = entity.blurhash,
            languages = entity.languages,
            contactEmail = entity.contactEmail,
            contactAccountId = entity.contactAccountId,
            rules = entity.rules,
            config = entity.config,
        )
    }

    override suspend fun getCachedInstanceInfo(): Flow<Instance> =
        queries.selectInstanceInfo(currentDomain)
            .asFlow()
            .mapToOne(coroutineContext)
            .map(InstanceInfoEntityMapper::fromEntity)
}
