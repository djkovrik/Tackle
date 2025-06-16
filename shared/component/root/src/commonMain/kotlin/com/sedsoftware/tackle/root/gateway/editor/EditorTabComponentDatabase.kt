package com.sedsoftware.tackle.root.gateway.editor

import com.sedsoftware.tackle.domain.api.TackleDatabase
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.EditorComponentGateways
import kotlinx.coroutines.flow.Flow

internal class EditorTabComponentDatabase(
    private val database: TackleDatabase,
) : EditorComponentGateways.Database {

    override suspend fun cacheServerEmojis(list: List<CustomEmoji>) = database.insertEmojis(list)
    override suspend fun observeCachedEmojis(): Flow<Map<String, List<CustomEmoji>>> = database.observeEmojis()
    override suspend fun findEmojis(query: String): Flow<List<CustomEmoji>> = database.findEmoji(query)
    override suspend fun getCachedInstanceInfo(): Flow<Instance> = database.getCachedInstanceInfo()
}
