package com.sedsoftware.tackle.domain.api

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import kotlinx.coroutines.flow.Flow

interface TackleDatabase {

    /**
     * Cache instance emojis to local database
     *
     * @param list instance emojis list
     */
    suspend fun insertEmojis(list: List<CustomEmoji>)

    /**
     * Observe for cached emojis
     */
    suspend fun observeEmojis(): Flow<Map<String, List<CustomEmoji>>>

    /**
     * Find emojis by given string query, searches through shortcodes
     */
    suspend fun findEmoji(query: String): Flow<List<CustomEmoji>>

    /**
     * Cache instance info to local database
     */
    suspend fun cacheInstanceInfo(info: Instance)

    /**
     * Observe for cached instance info
     */
    suspend fun getCachedInstanceInfo(): Flow<Instance>
}
