package com.sedsoftware.tackle.statuslist.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.items.Items
import com.arkivanov.decompose.router.items.ItemsNavigation
import com.arkivanov.decompose.router.items.LazyChildItems
import com.arkivanov.decompose.router.items.childItems
import com.arkivanov.decompose.router.items.setItems
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.Timeline
import com.sedsoftware.tackle.main.StatusComponent
import com.sedsoftware.tackle.main.StatusComponentGateways
import com.sedsoftware.tackle.main.integration.StatusComponentDefault
import com.sedsoftware.tackle.statuslist.StatusListComponent
import com.sedsoftware.tackle.statuslist.StatusListComponent.Model
import com.sedsoftware.tackle.statuslist.domain.StatusListManager
import com.sedsoftware.tackle.statuslist.store.StatusListStore
import com.sedsoftware.tackle.statuslist.store.StatusListStore.Label
import com.sedsoftware.tackle.statuslist.store.StatusListStoreProvider
import com.sedsoftware.tackle.utils.extension.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalDecomposeApi::class)
class StatusListComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: StatusComponentGateways.Api,
    private val settings: StatusComponentGateways.Settings,
    private val tools: StatusComponentGateways.Tools,
    private val dispatchers: TackleDispatchers,
    private val output: (ComponentOutput) -> Unit,
    private val onBackClicked: () -> Unit,
    private val timeline: Timeline,
) : StatusListComponent, ComponentContext by componentContext {

    private val store: StatusListStore =
        instanceKeeper.getStore {
            StatusListStoreProvider(
                storeFactory = storeFactory,
                manager = StatusListManager(timeline, api),
                mainContext = dispatchers.main,
                ioContext = dispatchers.io,
                timeline = timeline,
            ).create()
        }

    private val navigation: ItemsNavigation<Status> = ItemsNavigation<Status>()

    init {
        val scope = CoroutineScope(dispatchers.main)

        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is Label.ErrorCaught -> output(ComponentOutput.Common.ErrorCaught(label.exception))
                }
            }
        }

        scope.launch {
            store.states
                .map { it.items }
                .distinctUntilChanged()
                .catch { output(ComponentOutput.Common.ErrorCaught(it)) }
                .collect { newItems ->
                    navigation.setItems { newItems }
                }
        }

        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    override val model: Value<Model> = store.asValue().map(::stateToModel)

    override val items: LazyChildItems<Status, StatusComponent> =
        childItems(
            source = navigation,
            serializer = null,
            initialItems = { Items(items = store.state.items) },
            childFactory = ::createChild,
        )

    override fun onBack() {
        onBackClicked()
    }

    override fun onPullToRefresh() {
        store.accept(StatusListStore.Intent.OnPullToRefreshCalled)
    }

    override fun onLoadMoreRequest() {
        store.accept(StatusListStore.Intent.OnLoadMoreRequested)
    }

    override fun showCreatedStatus(status: Status) {
        store.accept(StatusListStore.Intent.StatusCreated(status))
    }

    private fun stateToModel(from: StatusListStore.State): Model =
        Model(
            timeline = from.timeline,
            initialProgressVisible = from.initialProgressVisible,
            loadMoreProgressVisible = from.loadMoreProgressVisible,
            emptyPlaceholderVisible = from.emptyPlaceholderVisible,
        )

    internal fun createChild(status: Status, ctx: ComponentContext): StatusComponent {
        val ownId: String = settings.ownUserId
        return StatusComponentDefault(
            componentContext = ctx,
            storeFactory = storeFactory,
            api = api,
            tools = tools,
            dispatchers = dispatchers,
            output = ::onStatusComponentOutput,
            status = status,
            extendedInfo = false,
            isOwn = status.account.id == ownId,
        )
    }

    internal fun onStatusComponentOutput(statusOutput: ComponentOutput) {
        if (statusOutput is ComponentOutput.SingleStatus.Deleted) {
            store.accept(StatusListStore.Intent.StatusDeleted(statusOutput.statusId))
        } else {
            output(statusOutput)
        }
    }
}
