package com.sedsoftware.tackle.statuslist.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.StoreCreate
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.Timeline
import com.sedsoftware.tackle.statuslist.domain.StatusListManager
import com.sedsoftware.tackle.statuslist.store.StatusListStore.Intent
import com.sedsoftware.tackle.statuslist.store.StatusListStore.Label
import com.sedsoftware.tackle.statuslist.store.StatusListStore.State
import com.sedsoftware.tackle.utils.extension.unwrap
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class StatusListStoreProvider(
    private val storeFactory: StoreFactory,
    private val manager: StatusListManager,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
    private val timeline: Timeline,
) {

    @StoreCreate
    fun create(autoInit: Boolean = true): StatusListStore =
        object : StatusListStore, Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
            name = "StatusListStore_$timeline",
            initialState = State(timeline),
            autoInit = autoInit,
            bootstrapper = coroutineBootstrapper(mainContext) {
                dispatch(Action.LoadNextTimelinePage(forceRefresh = true))
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
                onAction<Action.LoadNextTimelinePage> {
                    launch {
                        val lastLoadedItemId = if (!it.forceRefresh) state().lastLoadedItemId else null

                        dispatch(Msg.TimelinePageLoadingStarted(it.forceRefresh))

                        unwrap(
                            result = withContext(ioContext) { manager.loadStatusList(DEFAULT_PAGE_SIZE, lastLoadedItemId) },
                            onSuccess = { items: List<Status> ->
                                if (it.forceRefresh) {
                                    dispatch(Msg.NewTimelinePageLoaded(items))
                                } else {
                                    dispatch(Msg.NextTimelinePageLoaded(items))
                                }
                            },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                                dispatch(Msg.TimelinePageLoadingFailed)
                            },
                        )
                    }
                }

                onIntent<Intent.OnPullToRefreshCalled> {
                    forward(Action.LoadNextTimelinePage(forceRefresh = true))
                }

                onIntent<Intent.OnLoadMoreRequested> {
                    if (state().hasMoreItems) {
                        forward(Action.LoadNextTimelinePage(forceRefresh = false))
                    }
                }

                onIntent<Intent.StatusDeleted> {
                    dispatch(Msg.StatusDeleted(it.statusId))
                }

                onIntent<Intent.StatusCreated> {
                    dispatch(Msg.NewStatusCreated(it.status))
                }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.TimelinePageLoadingStarted -> copy(
                        initialProgressVisible = items.isEmpty() || msg.forcedRefresh,
                        loadMoreProgressVisible = items.isNotEmpty() && !msg.forcedRefresh,
                    )

                    is Msg.NewTimelinePageLoaded -> copy(
                        initialProgressVisible = false,
                        loadMoreProgressVisible = false,
                        emptyPlaceholderVisible = msg.items.isEmpty(),
                        items = msg.items,
                        hasMoreItems = msg.items.size == DEFAULT_PAGE_SIZE,
                        lastLoadedItemId = msg.items.lastOrNull()?.id.orEmpty(),
                    )

                    is Msg.NextTimelinePageLoaded -> copy(
                        initialProgressVisible = false,
                        loadMoreProgressVisible = false,
                        items = (items + msg.items).distinctBy { it.id },
                        hasMoreItems = msg.items.size == DEFAULT_PAGE_SIZE,
                        lastLoadedItemId = msg.items.lastOrNull()?.id.orEmpty(),
                    )

                    is Msg.TimelinePageLoadingFailed -> copy(
                        initialProgressVisible = false,
                        loadMoreProgressVisible = false,
                    )

                    is Msg.StatusDeleted -> copy(
                        items = items.filterNot { it.id == msg.statusId },
                    )

                    is Msg.NewStatusCreated -> copy(
                        items = listOf(msg.status) + items,
                    )
                }
            }
        ) {}

    private sealed interface Action {
        data class LoadNextTimelinePage(val forceRefresh: Boolean) : Action
    }

    private sealed interface Msg {
        data class TimelinePageLoadingStarted(val forcedRefresh: Boolean) : Msg
        data class NewTimelinePageLoaded(val items: List<Status>) : Msg
        data class NextTimelinePageLoaded(val items: List<Status>) : Msg
        data object TimelinePageLoadingFailed : Msg
        data class StatusDeleted(val statusId: String) : Msg
        data class NewStatusCreated(val status: Status) : Msg
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 25
    }
}
