package com.sedsoftware.tackle.statuslist.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.Timeline
import com.sedsoftware.tackle.status.StatusComponent
import com.sedsoftware.tackle.status.StatusComponentGateways
import com.sedsoftware.tackle.status.integration.StatusComponentDefault
import com.sedsoftware.tackle.statuslist.StatusListComponent
import com.sedsoftware.tackle.statuslist.domain.StatusListManager
import com.sedsoftware.tackle.statuslist.store.StatusListStore
import com.sedsoftware.tackle.statuslist.store.StatusListStore.Label
import com.sedsoftware.tackle.statuslist.store.StatusListStoreProvider
import com.sedsoftware.tackle.utils.extension.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
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

    override val model: Value<StatusListComponent.Model> = store.asValue().map(stateToModel)

    override val components: MutableValue<List<StatusComponent>> = MutableValue(emptyList())

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
                .distinctUntilChanged()
                .collect(::refreshComponentsList)
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

    private fun refreshComponentsList(state: StatusListStore.State) {
        val currentComponents = components.value
        val lastComponentStatusId: String = currentComponents.lastOrNull()?.model?.value?.status?.id.orEmpty()
        val lastComponentStatusPosition: Int = state.items.indexOfLast { it.id == lastComponentStatusId } + 1
        val newComponents = state.items.drop(lastComponentStatusPosition).map(::buildComponent)
        val resultingList = currentComponents + newComponents
        components.value = resultingList
    }

    internal fun buildComponent(status: Status): StatusComponent {
        val ownId: String = settings.ownUserId
        val resultingStatus = status.reblog.takeIf { it != null } ?: status
        val componentLifecycle = LifecycleRegistry()

        return StatusComponentDefault(
            componentContext = componentContext.childContext(key = status.id + "_$timeline", lifecycle = componentLifecycle),
            componentLifecycle = componentLifecycle,
            storeFactory = storeFactory,
            api = api,
            tools = tools,
            dispatchers = dispatchers,
            output = output,
            status = resultingStatus,
            rebloggedBy = status.reblog?.account?.displayName
                ?: status.reblog?.account?.username.orEmpty(),
            extendedInfo = false,
            isOwn = resultingStatus.id == ownId,
        )
    }
}
