package com.sedsoftware.tackle.main.integration.editor

import com.sedsoftware.tackle.domain.TackleDatabase
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.domain.model.CustomEmoji
import kotlinx.coroutines.flow.Flow

internal class EditorTabComponentDatabase(
    private val database: TackleDatabase,
) : EditorTabComponentGateways.Database {

    override suspend fun cacheServerEmojis(list: List<CustomEmoji>) {
        TODO("Not yet implemented")
    }

    override suspend fun observeCachedEmojis(): Flow<List<CustomEmoji>> {
        TODO("Not yet implemented")
    }
}
