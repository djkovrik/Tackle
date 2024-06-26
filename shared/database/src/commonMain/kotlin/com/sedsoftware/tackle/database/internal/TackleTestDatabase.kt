package com.sedsoftware.tackle.database.internal

import com.sedsoftware.tackle.domain.TackleDatabase
import com.sedsoftware.tackle.domain.model.CustomEmoji
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class TackleTestDatabase : TackleDatabase {
    private val emojiCache: Flow<List<CustomEmoji>>
        get() = _emojiCache

    private val _emojiCache: MutableStateFlow<List<CustomEmoji>> = MutableStateFlow(emptyList())

    override suspend fun insertEmojis(list: List<CustomEmoji>) {
        _emojiCache.value = list
    }

    override suspend fun observeEmojis(): Flow<List<CustomEmoji>> = emojiCache
}
