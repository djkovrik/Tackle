package com.sedsoftware.tackle.editor.domain

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Tag
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.stubs.EditorTabComponentApiStub
import com.sedsoftware.tackle.editor.stubs.EditorTabComponentDatabaseStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class EditorTabManagerTest {

    private val api: EditorTabComponentApiStub = EditorTabComponentApiStub()
    private val database: EditorTabComponentGateways.Database = EditorTabComponentDatabaseStub()
    private val manager: EditorTabManager = EditorTabManager(api, database)

    @Test
    fun `getCachedInstanceInfo should return instance info`() = runTest {
        // given
        // when
        val result = manager.getCachedInstanceInfo()
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow().domain).isNotEmpty()
    }

    @Test
    fun `searchForAccounts should return accounts successfully`() = runTest {
        // given
        val query = "query"
        // when
        val result: Result<List<Account>> = manager.searchForAccounts(query)
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isNotEmpty()
    }

    @Test
    fun `searchForAccounts should return failure on api error`() = runTest {
        // given
        val query = "query"
        api.shouldThrowException = true
        // when
        val result: Result<List<Account>> = manager.searchForAccounts(query)
        // then
        assertThat(result.isSuccess).isFalse()
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `searchForEmojis should return accounts successfully`() = runTest {
        // given
        val query = "query"
        // when
        val result: Result<List<CustomEmoji>> = manager.searchForEmojis(query)
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isNotEmpty()
    }

    @Test
    fun `searchForHashTags should return accounts successfully`() = runTest {
        // given
        val query = "query"
        // when
        val result: Result<List<Tag>> = manager.searchForHashTags(query)
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isNotEmpty()
    }

    @Test
    fun `searchForHashTags should return failure on api error`() = runTest {
        // given
        val query = "query"
        api.shouldThrowException = true
        // when
        val result: Result<List<Tag>> = manager.searchForHashTags(query)
        // then
        assertThat(result.isSuccess).isFalse()
        assertThat(result.isFailure).isTrue()
    }
}
