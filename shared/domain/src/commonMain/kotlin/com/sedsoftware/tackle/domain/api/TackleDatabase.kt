package com.sedsoftware.tackle.domain.api

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import kotlinx.coroutines.flow.Flow

interface TackleDatabase {
    suspend fun insertEmojis(list: List<CustomEmoji>)
    suspend fun observeEmojis(): Flow<Map<String, List<CustomEmoji>>>
    suspend fun findEmoji(query: String): Flow<List<CustomEmoji>>
    suspend fun cacheInstanceInfo(info: Instance)
    suspend fun getCachedInstanceInfo(): Flow<Instance>
}
