package com.sedsoftware.tackle.home.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.Timeline
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.main.StatusComponentGateways
import com.sedsoftware.tackle.statuslist.StatusListComponent
import com.sedsoftware.tackle.statuslist.integration.StatusListComponentDefault

class HomeTabComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: StatusComponentGateways.Api,
    private val settings: StatusComponentGateways.Settings,
    private val tools: StatusComponentGateways.Tools,
    private val dispatchers: TackleDispatchers,
    private val output: (ComponentOutput) -> Unit,
) : HomeTabComponent, ComponentContext by componentContext {

    override val homeTimeline: StatusListComponent =
        StatusListComponentDefault(
            componentContext = childContext(key = "Home timeline"),
            storeFactory = storeFactory,
            api = api,
            settings = settings,
            tools = tools,
            dispatchers = dispatchers,
            output = output,
            timeline = Timeline.Home,
        )

    override fun onNewPostClick() {
        output(ComponentOutput.HomeTab.EditorRequested)
    }

    override fun onScheduledPostsClick() {
        output(ComponentOutput.HomeTab.ScheduledStatusesRequested)
    }

    override fun showCreatedStatus(status: Status) {
        homeTimeline.showCreatedStatus(status)
    }
}
