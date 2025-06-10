package com.sedsoftware.tackle.status.domain

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.status.Responses
import com.sedsoftware.tackle.status.StatusComponentGateways
import com.sedsoftware.tackle.status.model.StatusContextAction
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class StatusManagerTest {

    private val api: StatusComponentGateways.Api = mock {
        everySuspend { delete(any(), any()) } returns Responses.status
        everySuspend { translate(any(), any()) } returns Responses.translation
        everySuspend { vote(any(), any()) } returns Responses.poll
        everySuspend { favourite(any()) } returns Responses.status.copy(favourited = true)
        everySuspend { unfavourite(any()) } returns Responses.status.copy(favourited = false)
        everySuspend { boost(any()) } returns Responses.status.copy(reblogged = true)
        everySuspend { unboost(any()) } returns Responses.status.copy(reblogged = false)
        everySuspend { bookmark(any()) } returns Responses.status.copy(bookmarked = true)
        everySuspend { unbookmark(any()) } returns Responses.status.copy(bookmarked = false)
        everySuspend { pin(any()) } returns Responses.status.copy(pinned = true)
        everySuspend { unpin(any()) } returns Responses.status.copy(pinned = false)
        everySuspend { mute(any()) } returns Responses.status.copy(muted = true)
        everySuspend { unmute(any()) } returns Responses.status.copy(muted = false)
    }

    private val tools: StatusComponentGateways.Tools = mock {
        every { getCurrentLocale() } returns AppLocale("English", "en")
    }

    private val manager: StatusManager = StatusManager(api, tools)

    @Test
    fun `delete should call for api`() = runTest {
        // given
        val testStatusId = "123"
        // when
        manager.delete(testStatusId, false)
        // then
        verifySuspend { api.delete(testStatusId, false) }
    }

    @Test
    fun `translate should call for api`() = runTest {
        // given
        val testStatusId = "123"
        val testLang = "en"
        // when
        manager.translate(testStatusId)
        // then
        verifySuspend { api.translate(testStatusId, testLang) }
    }

    @Test
    fun `vote should call for api`() = runTest {
        // given
        val testPollId = "123"
        val votes = listOf<Int>(0, 1)
        // when
        manager.vote(testPollId, votes)
        // then
        verifySuspend { api.vote(testPollId, votes) }
    }

    @Test
    fun `favourite calls should return proper result`() = runTest {
        // given
        val testStatusId = "123"
        // when
        var response = manager.favourite(testStatusId, true)
        // then
        verifySuspend { api.favourite(testStatusId) }
        assertThat(response.isSuccess).isTrue()
        assertThat(response.getOrThrow()).isTrue()
        // when
        response = manager.favourite(testStatusId, false)
        // then
        verifySuspend { api.unfavourite(testStatusId) }
        assertThat(response.isSuccess).isTrue()
        assertThat(response.getOrThrow()).isFalse()
    }

    @Test
    fun `boost calls should return proper result`() = runTest {
        // given
        val testStatusId = "123"
        // when
        var response = manager.boost(testStatusId, true)
        // then
        verifySuspend { api.boost(testStatusId) }
        assertThat(response.isSuccess).isTrue()
        assertThat(response.getOrThrow()).isTrue()
        // when
        response = manager.boost(testStatusId, false)
        // then
        verifySuspend { api.unboost(testStatusId) }
        assertThat(response.isSuccess).isTrue()
        assertThat(response.getOrThrow()).isFalse()
    }

    @Test
    fun `bookmark calls should return proper result`() = runTest {
        // given
        val testStatusId = "123"
        // when
        var response = manager.bookmark(testStatusId, true)
        // then
        verifySuspend { api.bookmark(testStatusId) }
        assertThat(response.isSuccess).isTrue()
        assertThat(response.getOrThrow()).isTrue()
        // when
        response = manager.bookmark(testStatusId, false)
        // then
        verifySuspend { api.unbookmark(testStatusId) }
        assertThat(response.isSuccess).isTrue()
        assertThat(response.getOrThrow()).isFalse()
    }

    @Test
    fun `pin calls should return proper result`() = runTest {
        // given
        val testStatusId = "123"
        // when
        var response = manager.pin(testStatusId, true)
        // then
        verifySuspend { api.pin(testStatusId) }
        assertThat(response.isSuccess).isTrue()
        assertThat(response.getOrThrow()).isTrue()
        // when
        response = manager.pin(testStatusId, false)
        // then
        verifySuspend { api.unpin(testStatusId) }
        assertThat(response.isSuccess).isTrue()
        assertThat(response.getOrThrow()).isFalse()
    }

    @Test
    fun `mute calls should return proper result`() = runTest {
        // given
        val testStatusId = "123"
        // when
        var response = manager.mute(testStatusId, true)
        // then
        verifySuspend { api.mute(testStatusId) }
        assertThat(response.isSuccess).isTrue()
        assertThat(response.getOrThrow()).isTrue()
        // when
        response = manager.mute(testStatusId, false)
        // then
        verifySuspend { api.unmute(testStatusId) }
        assertThat(response.isSuccess).isTrue()
        assertThat(response.getOrThrow()).isFalse()
    }

    @Test
    fun `buildContextActions should show translation for untranslated statuses`() = runTest {
        // given
        val status = Responses.status.copy(language = "ru")
        // when
        val actions = manager.buildContextActions(status = status, isOwn = false, translated = false).getOrThrow()
        // then
        assertThat(actions).contains(StatusContextAction.TRANSLATE)
    }

    @Test
    fun `buildContextActions should show original for translated statuses`() = runTest {
        // given
        val status = Responses.status.copy(language = "ru")
        // when
        val actions = manager.buildContextActions(status = status, isOwn = false, translated = true).getOrThrow()
        // then
        assertThat(actions).contains(StatusContextAction.SHOW_ORIGINAL)
    }

    @Test
    fun `buildContextActions should build proper actions list`() = runTest {
        // given
        val statusPinned = Responses.status.copy(pinned = true)
        val statusNotPinned = Responses.status.copy(pinned = false)
        val statusBookmarked = Responses.status.copy(bookmarked = true)
        val statusNotBookmarked = Responses.status.copy(bookmarked = false)
        val statusMuted = Responses.status.copy(muted = true)
        val statusNotMuted = Responses.status.copy(muted = false)
        // when
        val actionsPinned = manager.buildContextActions(status = statusPinned, isOwn = true, translated = false).getOrThrow()
        val actionsNotPinned = manager.buildContextActions(status = statusNotPinned, isOwn = true, translated = false).getOrThrow()
        val actionsBookmarked = manager.buildContextActions(status = statusBookmarked, isOwn = false, translated = false).getOrThrow()
        val actionsNotBookmarked = manager.buildContextActions(status = statusNotBookmarked, isOwn = false, translated = false).getOrThrow()
        val actionsMuted = manager.buildContextActions(status = statusMuted, isOwn = false, translated = false).getOrThrow()
        val actionsNotMuted = manager.buildContextActions(status = statusNotMuted, isOwn = false, translated = false).getOrThrow()
        // then
        assertThat(actionsPinned).contains(StatusContextAction.UNPIN)
        assertThat(actionsNotPinned).contains(StatusContextAction.PIN)
        assertThat(actionsBookmarked).contains(StatusContextAction.UNBOOKMARK)
        assertThat(actionsNotBookmarked).contains(StatusContextAction.BOOKMARK)
        assertThat(actionsMuted).contains(StatusContextAction.UNMUTE)
        assertThat(actionsNotMuted).contains(StatusContextAction.MUTE)
    }
}
