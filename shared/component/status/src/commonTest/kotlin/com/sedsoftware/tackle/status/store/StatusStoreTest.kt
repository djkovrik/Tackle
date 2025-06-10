package com.sedsoftware.tackle.status.store

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isNullOrEmpty
import assertk.assertions.isTrue
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.status.Responses
import com.sedsoftware.tackle.status.StatusComponentGateways
import com.sedsoftware.tackle.status.domain.StatusManager
import com.sedsoftware.tackle.utils.test.StoreTest
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class StatusStoreTest : StoreTest<StatusStore.Intent, StatusStore.State, StatusStore.Label>() {

    private val testStatus: Status = Responses.status

    private val api: StatusComponentGateways.Api = mock {
        everySuspend { delete(any(), any()) } returns testStatus
        everySuspend { translate(any(), any()) } returns Responses.translation
        everySuspend { vote(any(), any()) } returns Responses.poll
        everySuspend { favourite(any()) } returns testStatus.copy(favourited = true)
        everySuspend { unfavourite(any()) } returns testStatus.copy(favourited = false)
        everySuspend { boost(any()) } returns testStatus.copy(reblogged = true)
        everySuspend { unboost(any()) } returns testStatus.copy(reblogged = false)
        everySuspend { bookmark(any()) } returns testStatus.copy(bookmarked = true)
        everySuspend { unbookmark(any()) } returns testStatus.copy(bookmarked = false)
        everySuspend { pin(any()) } returns testStatus.copy(pinned = true)
        everySuspend { unpin(any()) } returns testStatus.copy(pinned = false)
        everySuspend { mute(any()) } returns testStatus.copy(muted = true)
        everySuspend { unmute(any()) } returns testStatus.copy(muted = false)
    }

    private val tools: StatusComponentGateways.Tools = mock {
        every { getCurrentLocale() } returns AppLocale("English", "en")
    }

    private val manager: StatusManager = StatusManager(api, tools)

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `store init should call for RefreshContextMenu on success`() = runTest {
        // given
        // when
        store.init()
        // then
        assertThat(store.state.menuActions).isNotEmpty()
    }

    @Test
    fun `store init should publish ErrorCaught label on failure`() = runTest {
        // given
        every { tools.getCurrentLocale() } throws IllegalStateException("Test")
        // when
        store.init()
        // then
        assertThat(store.state.menuActions).isEmpty()
        assertThat(labels.count { it is StatusStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `OnDeleteClicked should publish StatusDeleted label on success`() = runTest {
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnDeleteClicked)
        // then
        assertThat(labels.count { it is StatusStore.Label.StatusDeleted }).isEqualTo(1)
        val label = labels.firstOrNull { it is StatusStore.Label.StatusDeleted } as StatusStore.Label.StatusDeleted
        assertThat(label.statusId).isEqualTo(testStatus.id)
    }

    @Test
    fun `OnDeleteClicked should publish ErrorCaught label on failure`() = runTest {
        // given
        everySuspend { api.delete(any(), any()) } throws IllegalStateException("Test")
        // when
        store.init()
        store.accept(StatusStore.Intent.OnDeleteClicked)
        // then
        assertThat(labels.count { it is StatusStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `OnTranslateClicked should show translation on success`() = runTest {
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnTranslateClicked)
        // then
        assertThat(store.state.translation).isNotNull()
        assertThat(store.state.translationDisplayed).isTrue()
    }

    @Test
    fun `OnTranslateClicked should show existing translation on repeated calls`() = runTest {
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnTranslateClicked)
        // then
        assertThat(store.state.translation).isNotNull()
        assertThat(store.state.translationDisplayed).isTrue()
        // when
        store.accept(StatusStore.Intent.OnShowOriginalClicked)
        // then
        assertThat(store.state.translationDisplayed).isFalse()
        // when
        store.accept(StatusStore.Intent.OnTranslateClicked)
        // then
        assertThat(store.state.translationDisplayed).isTrue()
        verifySuspend(exactly(1)) { api.translate(any(), any()) }
    }

    @Test
    fun `OnTranslateClicked should publish ErrorCaught label on failure`() = runTest {
        // given
        everySuspend { api.translate(any(), any()) } throws IllegalStateException("Test")
        store.init()
        // when
        store.accept(StatusStore.Intent.OnTranslateClicked)
        // then
        assertThat(labels.count { it is StatusStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `OnFavouriteClicked should favourite status on success`() = runTest {
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnFavouriteClicked)
        // then
        verifySuspend(exactly(1)) { api.favourite(testStatus.id) }
        assertThat(store.state.status.favourited).isTrue()
        // when
        store.accept(StatusStore.Intent.OnFavouriteClicked)
        // then
        verifySuspend(exactly(1)) { api.unfavourite(testStatus.id) }
        assertThat(store.state.status.favourited).isFalse()
    }

    @Test
    fun `OnFavouriteClicked should publish ErrorCaught label on failure`() = runTest {
        everySuspend { api.favourite(any()) } throws IllegalStateException("Test")
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnFavouriteClicked)
        // then
        assertThat(labels.count { it is StatusStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `OnReblogClicked should reblog status on success`() = runTest {
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnReblogClicked)
        // then
        verifySuspend(exactly(1)) { api.boost(testStatus.id) }
        assertThat(store.state.status.reblogged).isTrue()
        // when
        store.accept(StatusStore.Intent.OnReblogClicked)
        // then
        verifySuspend(exactly(1)) { api.unboost(testStatus.id) }
        assertThat(store.state.status.reblogged).isFalse()
    }

    @Test
    fun `OnReblogClicked should publish ErrorCaught label on failure`() = runTest {
        everySuspend { api.boost(any()) } throws IllegalStateException("Test")
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnReblogClicked)
        // then
        assertThat(labels.count { it is StatusStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `OnBookmarkClicked should bookmark status on success`() = runTest {
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnBookmarkClicked)
        // then
        verifySuspend(exactly(1)) { api.bookmark(testStatus.id) }
        assertThat(store.state.status.bookmarked).isTrue()
        // when
        store.accept(StatusStore.Intent.OnBookmarkClicked)
        // then
        verifySuspend(exactly(1)) { api.unbookmark(testStatus.id) }
        assertThat(store.state.status.bookmarked).isFalse()
    }

    @Test
    fun `OnBookmarkClicked should publish ErrorCaught label on failure`() = runTest {
        everySuspend { api.bookmark(any()) } throws IllegalStateException("Test")
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnBookmarkClicked)
        // then
        assertThat(labels.count { it is StatusStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `OnPinClicked should pin status on success`() = runTest {
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnPinClicked)
        // then
        verifySuspend(exactly(1)) { api.pin(testStatus.id) }
        assertThat(store.state.status.pinned).isTrue()
        // when
        store.accept(StatusStore.Intent.OnPinClicked)
        // then
        verifySuspend(exactly(1)) { api.unpin(testStatus.id) }
        assertThat(store.state.status.pinned).isFalse()
    }

    @Test
    fun `OnPinClicked should publish ErrorCaught label on failure`() = runTest {
        everySuspend { api.pin(any()) } throws IllegalStateException("Test")
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnPinClicked)
        // then
        assertThat(labels.count { it is StatusStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `OnMuteClicked should mute status on success`() = runTest {
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnMuteClicked)
        // then
        verifySuspend(exactly(1)) { api.mute(testStatus.id) }
        assertThat(store.state.status.muted).isTrue()
        // when
        store.accept(StatusStore.Intent.OnMuteClicked)
        // then
        verifySuspend(exactly(1)) { api.unmute(testStatus.id) }
        assertThat(store.state.status.muted).isFalse()
    }

    @Test
    fun `OnMuteClicked should publish ErrorCaught label on failure`() = runTest {
        everySuspend { api.mute(any()) } throws IllegalStateException("Test")
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnMuteClicked)
        // then
        assertThat(labels.count { it is StatusStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `OnMenuVisibilityChanged should update menu visibility`() = runTest {
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnMenuVisibilityChanged(true))
        // then
        assertThat(store.state.menuVisible).isTrue()
        // when
        store.accept(StatusStore.Intent.OnMenuVisibilityChanged(false))
        // then
        assertThat(store.state.menuVisible).isFalse()
    }

    @Test
    fun `OnPollOptionSelected should update poll state`() = runTest {
        // given
        store.init()
        // when
        store.accept(StatusStore.Intent.OnPollOptionSelected(1, true))
        store.accept(StatusStore.Intent.OnPollOptionSelected(0, true))
        // then
        assertThat(store.state.status.poll?.ownVotes).isEqualTo(listOf(0, 1))
        // when
        store.accept(StatusStore.Intent.OnPollOptionSelected(1, true))
        // then
        assertThat(store.state.status.poll?.ownVotes).isEqualTo(listOf(0))
        // when
        store.accept(StatusStore.Intent.OnPollOptionSelected(0, true))
        // then
        assertThat(store.state.status.poll?.ownVotes).isNullOrEmpty()
        // when
        store.accept(StatusStore.Intent.OnPollOptionSelected(1, false))
        store.accept(StatusStore.Intent.OnPollOptionSelected(0, false))
        assertThat(store.state.status.poll?.ownVotes).isEqualTo(listOf(0))
    }

    @Test
    fun `OnVoteClicked should send vote request on success`() = runTest {
        // given
        store.init()
        store.accept(StatusStore.Intent.OnPollOptionSelected(1, true))
        store.accept(StatusStore.Intent.OnPollOptionSelected(0, true))
        // when
        store.accept(StatusStore.Intent.OnVoteClicked)
        // then
        assertThat(store.state.status.poll?.voted).isEqualTo(true)
    }

    @Test
    fun `OnVoteClicked should publish ErrorCaught label on failure`() = runTest {
        // given
        everySuspend { api.vote(any(), any()) } throws IllegalStateException("Test")
        store.init()
        store.accept(StatusStore.Intent.OnPollOptionSelected(1, true))
        store.accept(StatusStore.Intent.OnPollOptionSelected(0, true))
        // when
        store.accept(StatusStore.Intent.OnVoteClicked)
        // then
        assertThat(labels.count { it is StatusStore.Label.ErrorCaught }).isEqualTo(1)
    }

    override fun createStore(): Store<StatusStore.Intent, StatusStore.State, StatusStore.Label> =
        StatusStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = manager,
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
            status = testStatus,
            isOwn = true,
        ).create(autoInit = false)
}
