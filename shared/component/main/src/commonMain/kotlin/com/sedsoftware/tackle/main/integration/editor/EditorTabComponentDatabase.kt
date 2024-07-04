package com.sedsoftware.tackle.main.integration.editor

import com.sedsoftware.tackle.domain.api.TackleDatabase
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import kotlinx.coroutines.flow.Flow

internal class EditorTabComponentDatabase(
    private val database: TackleDatabase,
) : EditorTabComponentGateways.Database {

    override suspend fun cacheServerEmojis(list: List<CustomEmoji>) = database.insertEmojis(list)
    override suspend fun observeCachedEmojis(): Flow<List<CustomEmoji>> = database.observeEmojis()
}
