package com.sedsoftware.tackle.publications.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.model.type.Timeline
import com.sedsoftware.tackle.publications.PublicationsTabComponent
import com.sedsoftware.tackle.publications.PublicationsTabComponent.Child
import com.sedsoftware.tackle.status.StatusComponentGateways
import com.sedsoftware.tackle.statuslist.integration.StatusListComponentDefault
import kotlinx.serialization.Serializable

class PublicationsTabComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: StatusComponentGateways.Api,
    private val settings: StatusComponentGateways.Settings,
    private val tools: StatusComponentGateways.Tools,
    private val dispatchers: TackleDispatchers,
    private val output: (ComponentOutput) -> Unit,
) : PublicationsTabComponent, ComponentContext by componentContext {

    private val navigation: StackNavigation<Config> = StackNavigation()

    private val stack: Value<ChildStack<Config, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.LocalTimeline,
            handleBackButton = false,
            childFactory = ::createChild,
        )

    override val childStack: Value<ChildStack<*, Child>> = stack

    override fun onLocalTabClick() {
        navigation.bringToFront(Config.LocalTimeline)
    }

    override fun onRemoteTabClick() {
        navigation.bringToFront(Config.RemoteTimeline)
    }

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.LocalTimeline ->
                Child.LocalTimeline(
                    StatusListComponentDefault(
                        componentContext = componentContext,
                        storeFactory = storeFactory,
                        api = api,
                        settings = settings,
                        tools = tools,
                        dispatchers = dispatchers,
                        output = output,
                        timeline = Timeline.Public(true),
                    )
                )

            is Config.RemoteTimeline ->
                Child.RemoteTimeline(
                    StatusListComponentDefault(
                        componentContext = componentContext,
                        storeFactory = storeFactory,
                        api = api,
                        settings = settings,
                        tools = tools,
                        dispatchers = dispatchers,
                        output = output,
                        timeline = Timeline.Public(false),
                    )
                )
        }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object LocalTimeline : Config

        @Serializable
        data object RemoteTimeline : Config
    }
}
