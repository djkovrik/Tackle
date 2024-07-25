package com.sedsoftware.tackle.database.internal

import com.sedsoftware.tackle.domain.api.TackleDatabase
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

internal class TackleTestDatabase : TackleDatabase {
    private val emojiCache: Flow<List<CustomEmoji>>
        get() = _emojiCache

    private val _emojiCache: MutableStateFlow<List<CustomEmoji>> = MutableStateFlow(emptyList())

    private val instanceInfoCache: Flow<Instance>
        get() = _instanceInfoCache

    private val _instanceInfoCache: MutableStateFlow<Instance> = MutableStateFlow(Instance.empty())

    override suspend fun insertEmojis(list: List<CustomEmoji>) {
        _emojiCache.value = list
    }

    override suspend fun cacheInstanceInfo(info: Instance) {
        _instanceInfoCache.value = info
    }

    override suspend fun observeEmojis(): Flow<Map<String, List<CustomEmoji>>> = emojiCache.map(::mapList)

    override suspend fun getCachedInstanceInfo(): Flow<Instance> = instanceInfoCache

    private fun mapList(from: List<CustomEmoji>): Map<String, List<CustomEmoji>> = from.groupBy { it.category }
}
