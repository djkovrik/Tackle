package com.sedsoftware.tackle.statuslist.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.statuslist.store.StatusListStore.Intent
import com.sedsoftware.tackle.statuslist.store.StatusListStore.Label
import com.sedsoftware.tackle.statuslist.store.StatusListStore.State

interface StatusListStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnPullToRefreshCalled : Intent
        data object OnLoadMoreRequested : Intent
        data class StatusDeleted(val statusId: String) : Intent
        data object ScrollToTopRequested : Intent
    }

    data class State(
        val items: List<Status> = emptyList(),
        val initialProgressVisible: Boolean = true,
        val loadMoreProgressVisible: Boolean = false,
        val emptyPlaceholderVisible: Boolean = false,
        val hasMoreItems: Boolean = false,
        val lastLoadedItemId: String = "",
        val scrollRequests: Int = 0,
    )

    sealed class Label {
        data class ErrorCaught(val exception: Throwable) : Label()
    }
}
