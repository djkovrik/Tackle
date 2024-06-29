package com.sedsoftware.tackle.domain.api

import com.sedsoftware.tackle.domain.model.CustomEmoji
import kotlinx.coroutines.flow.Flow

interface TackleDatabase {
    suspend fun insertEmojis(list: List<CustomEmoji>)
    suspend fun observeEmojis(): Flow<List<CustomEmoji>>
}
