package com.sedsoftware.tackle.editor.integration.emojis

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.emojis.EditorEmojisGateways
import kotlinx.coroutines.flow.Flow

internal class EditorEmojisComponentDatabase(
    private val database: EditorTabComponentGateways.Database,
) : EditorEmojisGateways.Database {

    override suspend fun insertEmojis(list: List<CustomEmoji>) = database.cacheServerEmojis(list)
    override suspend fun observeEmojis(): Flow<Map<String, List<CustomEmoji>>> = database.observeCachedEmojis()
}
