package com.sedsoftware.tackle.editor.stubs

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.utils.test.StubWithException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EditorTabComponentDatabaseStub : StubWithException(), EditorTabComponentGateways.Database {

    var cachedEmoji: List<CustomEmoji> = emptyList()

    override suspend fun observeCachedEmojis(): Flow<Map<String, List<CustomEmoji>>> = flowOf(cachedEmoji.groupBy { it.category })

    override suspend fun getCachedInstanceInfo(): Flow<Instance> = flowOf(InstanceStub.instance)

    override suspend fun cacheServerEmojis(list: List<CustomEmoji>) {
        cachedEmoji = list
    }
}
