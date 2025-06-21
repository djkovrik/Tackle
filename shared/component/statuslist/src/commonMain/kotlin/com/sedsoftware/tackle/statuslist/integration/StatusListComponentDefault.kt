package com.sedsoftware.tackle.statuslist.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.Timeline
import com.sedsoftware.tackle.status.StatusComponent
import com.sedsoftware.tackle.status.StatusComponentGateways
import com.sedsoftware.tackle.status.integration.StatusComponentDefault
import com.sedsoftware.tackle.statuslist.StatusListComponent
import com.sedsoftware.tackle.statuslist.StatusListComponent.Model
import com.sedsoftware.tackle.statuslist.domain.StatusListManager
import com.sedsoftware.tackle.statuslist.store.StatusListStore
import com.sedsoftware.tackle.statuslist.store.StatusListStore.Label
import com.sedsoftware.tackle.statuslist.store.StatusListStoreProvider
import com.sedsoftware.tackle.utils.extension.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class StatusListComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: StatusComponentGateways.Api,
    private val settings: StatusComponentGateways.Settings,
    private val tools: StatusComponentGateways.Tools,
    private val dispatchers: TackleDispatchers,
    private val output: (ComponentOutput) -> Unit,
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

    private val internalComponentsMap: MutableMap<String, StatusComponent> = mutableMapOf()

    override val model: Value<Model> = store.asValue().map(::stateToModel)

    override val components: Value<List<StatusComponent>> = store.asValue().map(::stateToComponents)

    init {
        val scope = CoroutineScope(dispatchers.main)

        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is Label.ErrorCaught -> output(ComponentOutput.Common.ErrorCaught(label.exception))
                }
            }
        }

        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    override fun onPullToRefresh() {
        store.accept(StatusListStore.Intent.OnPullToRefreshCalled)
    }

    override fun onLoadMoreRequest() {
        store.accept(StatusListStore.Intent.OnLoadMoreRequested)
    }

    private fun stateToModel(from: StatusListStore.State): Model =
        Model(
            initialProgressVisible = from.initialProgressVisible,
            loadMoreProgressVisible = from.loadMoreProgressVisible,
            emptyPlaceholderVisible = from.emptyPlaceholderVisible,
            scrollRequests = from.scrollRequests,
        )

    private fun stateToComponents(from: StatusListStore.State): List<StatusComponent> =
        from.items.map { statusItem: Status ->
            internalComponentsMap[statusItem.id]?.apply { refreshStatus(statusItem) } ?: buildNewComponent(statusItem)
        }

    private fun onStatusComponentOutput(statusOutput: ComponentOutput) {
        if (statusOutput is ComponentOutput.SingleStatus.Deleted) {
            deleteExistingComponent(statusOutput.statusId)
            store.accept(StatusListStore.Intent.StatusDeleted(statusOutput.statusId))
        } else {
            output(statusOutput)
        }
    }

    private fun buildNewComponent(status: Status): StatusComponent {
        val ownId: String = settings.ownUserId
        val componentLifecycle = LifecycleRegistry()

        val createdComponent = StatusComponentDefault(
            componentContext = componentContext.childContext(key = status.id + "_$timeline", lifecycle = componentLifecycle),
            componentLifecycle = componentLifecycle,
            storeFactory = storeFactory,
            api = api,
            tools = tools,
            dispatchers = dispatchers,
            output = ::onStatusComponentOutput,
            status = status,
            rebloggedBy = when {
                status.reblog != null && status.account.displayName.isNotEmpty() -> status.account.displayName
                status.reblog != null -> status.account.username
                else -> ""
            },
            extendedInfo = false,
            isOwn = status.account.id == ownId,
        )

        internalComponentsMap[createdComponent.getId()] = createdComponent
        return createdComponent
    }

    private fun deleteExistingComponent(statusId: String) {
        internalComponentsMap[statusId]?.stopComponent()
        internalComponentsMap.remove(statusId)
    }
}
