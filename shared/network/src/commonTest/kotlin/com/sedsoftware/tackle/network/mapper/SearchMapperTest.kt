package com.sedsoftware.tackle.network.mapper

import assertk.assertThat
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.HashTag
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.network.response.SearchResponse
import com.sedsoftware.tackle.network.responseFromFile
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class SearchMapperTest {

    @Test
    fun `SearchMapper should map accounts response to entity`() = runTest {
        // given
        val response: SearchResponse = responseFromFile("src/commonTest/resources/responses/api_v2_search_accounts.json")
        // when
        val mapped: Search = SearchMapper.map(response)
        // then
        assertThat(mapped.accounts).isNotEmpty()
        mapped.accounts.forEach { account: Account ->
            assertThat(account.id).isNotEmpty()
            assertThat(account.username).isNotEmpty()
            assertThat(account.acct).isNotEmpty()
            assertThat(account.displayName).isNotEmpty()
            assertThat(account.locked).isNotNull()
            assertThat(account.locked).isNotNull()
            assertThat(account.emojis).isNotNull()
            assertThat(account.bot).isNotNull()
            assertThat(account.discoverable).isNotNull()
            assertThat(account.noIndex).isNotNull()
            assertThat(account.group).isNotNull()
            assertThat(account.createdAt).isNotNull()
            assertThat(account.note).isNotEmpty()
            assertThat(account.url).isNotEmpty()
            assertThat(account.uri).isNotEmpty()
            assertThat(account.avatar).isNotEmpty()
            assertThat(account.avatarStatic).isNotEmpty()
            assertThat(account.header).isNotEmpty()
            assertThat(account.headerStatic).isNotEmpty()
            assertThat(account.followersCount).isNotNull()
            assertThat(account.followingCount).isNotNull()
            assertThat(account.statusesCount).isNotNull()
        }
    }

    @Test
    fun `SearchMapper should map hashtags response to entity`() = runTest {
        // given
        val response: SearchResponse = responseFromFile("src/commonTest/resources/responses/api_v2_search_hashtags.json")
        // when
        val mapped: Search = SearchMapper.map(response)
        // then
        assertThat(mapped.hashtags).isNotEmpty()
        mapped.hashtags.forEach { hashtag: HashTag ->
            assertThat(hashtag.name).isNotEmpty()
            assertThat(hashtag.url).isNotEmpty()
            assertThat(hashtag.history).isNotEmpty()
        }
    }

    @Test
    fun `SearchMapper should map statuses response to entity`() = runTest {
        // given
        val response: SearchResponse = responseFromFile("src/commonTest/resources/responses/api_v2_search_statuses.json")
        // when
        val mapped: Search = SearchMapper.map(response)
        // then
        assertThat(mapped.statuses).isNotEmpty()
        mapped.statuses.forEach { status: Status ->
            assertThat(status.id).isNotEmpty()
            assertThat(status.createdAt).isNotNull()
            assertThat(status.sensitive).isNotNull()
            assertThat(status.visibility).isNotNull()
            assertThat(status.language).isNotEmpty()
            assertThat(status.uri).isNotEmpty()
            assertThat(status.url).isNotEmpty()
            assertThat(status.repliesCount).isNotNull()
            assertThat(status.reblogsCount).isNotNull()
            assertThat(status.muted).isNotNull()
            assertThat(status.bookmarked).isNotNull()
            assertThat(status.content).isNotEmpty()
            assertThat(status.account).isNotNull()
        }
    }
}
