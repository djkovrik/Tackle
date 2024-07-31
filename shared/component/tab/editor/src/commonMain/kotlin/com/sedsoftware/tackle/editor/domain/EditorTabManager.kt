package com.sedsoftware.tackle.editor.domain

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.domain.model.Tag
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import kotlinx.coroutines.flow.first

internal class EditorTabManager(
    private val api: EditorTabComponentGateways.Api,
    private val database: EditorTabComponentGateways.Database,
) {

    suspend fun getCachedInstanceInfo(): Result<Instance> = runCatching {
        database.getCachedInstanceInfo().first()
    }

    suspend fun searchForAccounts(query: String): Result<List<Account>> = runCatching {
        val response: Search = api.search(
            query = query,
            type = SEARCH_TYPE_ACCOUNT,
            resolve = false,
            limit = SEARCH_LIMIT,
        )

        response.accounts
    }

    suspend fun searchForEmojis(query: String): Result<List<CustomEmoji>> = runCatching {
        val response: List<CustomEmoji> = database.findEmojis(query).first()
        response.take(SEARCH_LIMIT)
    }

    suspend fun searchForHashTags(query: String): Result<List<Tag>> = runCatching {
        val response: Search = api.search(
            query = query,
            type = SEARCH_TYPE_HASHTAG,
            excludeUnreviewed = true,
            limit = SEARCH_LIMIT,
        )

        response.hashtags
    }

    private companion object {
        const val SEARCH_TYPE_ACCOUNT = "accounts"
        const val SEARCH_TYPE_HASHTAG = "hashtags"
        const val SEARCH_LIMIT = 10
    }
}
