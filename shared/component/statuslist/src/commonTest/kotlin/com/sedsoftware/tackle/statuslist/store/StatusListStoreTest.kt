package com.sedsoftware.tackle.statuslist.store

import assertk.assertThat
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.Timeline
import com.sedsoftware.tackle.status.StatusComponentGateways
import com.sedsoftware.tackle.statuslist.Responses
import com.sedsoftware.tackle.statuslist.domain.StatusListManager
import com.sedsoftware.tackle.utils.test.StoreTest
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class StatusListStoreTest : StoreTest<StatusListStore.Intent, StatusListStore.State, StatusListStore.Label>() {

    private val timeline: Timeline = Timeline.Home

    private val api: StatusComponentGateways.Api = mock {
        everySuspend { homeTimeline(any()) } returns List(StatusListStoreProvider.DEFAULT_PAGE_SIZE) { Responses.status.copy(id = "$it") }
    }

    private val manager: StatusListManager = StatusListManager(
        timeline = timeline,
        api = api,
    )

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `store init should load initial status list`() = runTest {
        // given
        // when
        store.init()
        // then
        assertThat(store.state.items.size).isEqualTo(StatusListStoreProvider.DEFAULT_PAGE_SIZE)
        assertThat(store.state.hasMoreItems).isTrue()
        assertThat(store.state.initialProgressVisible).isFalse()
        assertThat(store.state.loadMoreProgressVisible).isFalse()
        assertThat(store.state.lastLoadedItemId).isNotEmpty()
    }

    @Test
    fun `store init should publish ErrorCaught label on failure`() = runTest {
        // given
        everySuspend { api.homeTimeline(any()) } throws IllegalStateException("Test")
        // when
        store.init()
        // then
        assertThat(labels.count { it is StatusListStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `empty timeline should show no items placeholder`() = runTest {
        // given
        everySuspend { api.homeTimeline(any()) } returns emptyList()
        // when
        store.init()
        // then
        assertThat(store.state.initialProgressVisible).isFalse()
        assertThat(store.state.emptyPlaceholderVisible).isTrue()
    }


    @Test
    fun `OnPullToRefreshCalled should refresh items list`() = runTest {
        // given
        store.init()
        // when
        store.accept(StatusListStore.Intent.OnPullToRefreshCalled)
        // then
        assertThat(store.state.items.size).isEqualTo(StatusListStoreProvider.DEFAULT_PAGE_SIZE)
    }

    @Test
    fun `OnPullToRefreshCalled should publish ErrorCaught label on failure`() = runTest {
        // given
        store.init()
        everySuspend { api.homeTimeline(any()) } throws IllegalStateException("Test")
        // when
        store.accept(StatusListStore.Intent.OnPullToRefreshCalled)
        // then
        assertThat(labels.count { it is StatusListStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `OnLoadMoreRequested should append items list`() = runTest {
        // given
        store.init()
        // when
        store.accept(StatusListStore.Intent.OnLoadMoreRequested)
        // then
        assertThat(store.state.items.size).isEqualTo(StatusListStoreProvider.DEFAULT_PAGE_SIZE * 2)
        assertThat(store.state.hasMoreItems).isTrue()
    }

    @Test
    fun `OnLoadMoreRequested should set hasMoreItems to false on last page`() = runTest {
        // given
        store.init()
        // when
        store.accept(StatusListStore.Intent.OnLoadMoreRequested)
        // then
        assertThat(store.state.hasMoreItems).isTrue()
        // when
        everySuspend { api.homeTimeline(any()) } returns List(5) { Responses.status.copy(id = "$it") }
        store.accept(StatusListStore.Intent.OnLoadMoreRequested)
        // then
        assertThat(store.state.hasMoreItems).isFalse()
    }

    @Test
    fun `OnLoadMoreRequested should publish ErrorCaught label on failure`() = runTest {
        // given
        store.init()
        everySuspend { api.homeTimeline(any()) } throws IllegalStateException("Test")
        // when
        store.accept(StatusListStore.Intent.OnLoadMoreRequested)
        // then
        assertThat(labels.count { it is StatusListStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `StatusDeleted should delete item from state`() = runTest {
        // given
        store.init()
        assertThat(store.state.items.size).isEqualTo(StatusListStoreProvider.DEFAULT_PAGE_SIZE)
        assertThat(store.state.hasMoreItems).isTrue()
        val itemToDelete: Status = store.state.items[1]
        assertThat(store.state.items.contains(itemToDelete))
        // when
        store.accept(StatusListStore.Intent.StatusDeleted(itemToDelete.id))
        // then
        assertThat(store.state.items).doesNotContain(itemToDelete)
    }

    @Test
    fun `NewStatusCreated should prepend new status to beginning of the items list`() = runTest {
        // given
        store.init()
        val newStatus = Responses.status.copy(id = "new status")
        // when
        store.accept(StatusListStore.Intent.StatusCreated(newStatus))
        // then
        assertThat(store.state.items.first()).isEqualTo(newStatus)
    }


    @Test
    fun `ScrollToTopRequested should increase scroll requests count`() = runTest {
        // given
        store.init()
        assertThat(store.state.scrollRequests).isEqualTo(0)
        // when
        store.accept(StatusListStore.Intent.ScrollToTopRequested)
        // then
        assertThat(store.state.scrollRequests).isEqualTo(1)
    }


    override fun createStore(): Store<StatusListStore.Intent, StatusListStore.State, StatusListStore.Label> =
        StatusListStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = manager,
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
            timeline = timeline
        ).create(autoInit = false)
}
