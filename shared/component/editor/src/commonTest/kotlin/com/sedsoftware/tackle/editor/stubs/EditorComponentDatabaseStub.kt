package com.sedsoftware.tackle.editor.stubs

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.EditorComponentGateways
import com.sedsoftware.tackle.utils.test.StubWithException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EditorComponentDatabaseStub : StubWithException(), EditorComponentGateways.Database {

    companion object {
        val customEmojiList = listOf(
            CustomEmoji("test1", "test1", "test1", true, "test1"),
            CustomEmoji("test2", "test2", "test2", true, "test1"),
            CustomEmoji("test3", "test3", "test3", true, "test1"),
            CustomEmoji("abcde", "abcde", "abcde", true, "abcde"),
        )
    }

    var emojisCache: List<CustomEmoji> = customEmojiList

    override suspend fun observeCachedEmojis(): Flow<Map<String, List<CustomEmoji>>> = flowOf(emojisCache.groupBy { it.category })

    override suspend fun getCachedInstanceInfo(): Flow<Instance> = flowOf(InstanceStub.instance)

    override suspend fun findEmojis(query: String): Flow<List<CustomEmoji>> = flowOf(emojisCache)

    override suspend fun cacheServerEmojis(list: List<CustomEmoji>) {
        emojisCache = list
    }
}
