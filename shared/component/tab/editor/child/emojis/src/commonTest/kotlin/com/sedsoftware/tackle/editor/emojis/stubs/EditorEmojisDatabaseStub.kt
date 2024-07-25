package com.sedsoftware.tackle.editor.emojis.stubs

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.emojis.EditorEmojisGateways
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EditorEmojisDatabaseStub : EditorEmojisGateways.Database {

    var cachedEmoji: List<CustomEmoji> = emptyList()

    override suspend fun insertEmojis(list: List<CustomEmoji>) {
        cachedEmoji = list
    }

    override suspend fun observeEmojis(): Flow<Map<String, List<CustomEmoji>>> = flowOf(cachedEmoji.groupBy { it.category })
}
