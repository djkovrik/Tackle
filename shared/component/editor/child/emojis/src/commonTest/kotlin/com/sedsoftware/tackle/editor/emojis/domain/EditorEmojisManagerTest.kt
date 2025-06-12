package com.sedsoftware.tackle.editor.emojis.domain

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import com.sedsoftware.tackle.editor.emojis.EditorEmojisGateways
import com.sedsoftware.tackle.editor.emojis.Responses
import com.sedsoftware.tackle.editor.emojis.stubs.EditorEmojisDatabaseStub
import com.sedsoftware.tackle.editor.emojis.stubs.EditorEmojisSettingsStub
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.AfterTest
import kotlin.test.Test

class EditorEmojisManagerTest {

    private val api: EditorEmojisGateways.Api = mock {
        everySuspend { getServerEmojis() } returns Responses.correctResponse
    }

    private val db: EditorEmojisDatabaseStub = EditorEmojisDatabaseStub()

    private val settings: EditorEmojisSettingsStub = EditorEmojisSettingsStub()

    private val today: LocalDate
        get() = _today

    private var _today: LocalDate = LocalDate.parse("2024-01-01")

    private val manager: EditorEmojisManager = EditorEmojisManager(
        api = api,
        database = db,
        settings = settings,
        today = today,
    )

    @AfterTest
    fun after() {
        db.cachedEmoji = emptyList()
        settings.lastCachedTimestamp = ""
    }

    @Test
    fun `fetchServerEmojis should fetch emojis for the new day`() = runTest {
        // given
        settings.lastCachedTimestamp = ""
        _today = LocalDate.parse("2024-01-01")
        // when
        manager.fetchServerEmojis()
        // then
        assertThat(db.cachedEmoji).isNotEmpty()
        assertThat(settings.lastCachedTimestamp).isEqualTo(_today.toString())
    }

    @Test
    fun `fetchServerEmojis should not fetch emojis for the same day`() = runTest {
        // given
        settings.lastCachedTimestamp = "2024-01-01"
        _today = LocalDate.parse("2024-01-01")
        db.cachedEmoji = emptyList()
        // when
        manager.fetchServerEmojis()
        // then
        assertThat(db.cachedEmoji).isEmpty()
    }

    @Test
    fun `observeCachedEmojis should return cached emojis`() = runTest {
        // given
        settings.lastCachedTimestamp = ""
        _today = LocalDate.parse("2024-01-01")
        manager.fetchServerEmojis()
        // when
        val response = manager.observeCachedEmojis().first()
        // then
        assertThat(response).isNotEmpty()
    }
}
