package com.sedsoftware.tackle.main.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.browse.BrowseTabComponent
import com.sedsoftware.tackle.browse.integration.BrowseTabComponentDefault
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.integration.EditorTabComponentDefault
import com.sedsoftware.tackle.feeds.FeedsTabComponent
import com.sedsoftware.tackle.feeds.integration.FeedsTabComponentDefault
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.home.integration.HomeTabComponentDefault
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.MainComponent.Child
import com.sedsoftware.tackle.notifications.NotificationsTabComponent
import com.sedsoftware.tackle.notifications.integration.NotificationsTabComponentDefault
import kotlinx.serialization.Serializable

class MainComponentDefault internal constructor(
    private val componentContext: ComponentContext,
    private val mainComponentOutput: (MainComponent.Output) -> Unit,
    private val homeTabComponent: (ComponentContext, (HomeTabComponent.Output) -> Unit) -> HomeTabComponent,
    private val browseTabComponent: (ComponentContext, (BrowseTabComponent.Output) -> Unit) -> BrowseTabComponent,
    private val editorTabComponent: (ComponentContext, (EditorTabComponent.Output) -> Unit) -> EditorTabComponent,
    private val feedsTabComponent: (ComponentContext, (FeedsTabComponent.Output) -> Unit) -> FeedsTabComponent,
    private val notificationsTabComponent: (ComponentContext, (NotificationsTabComponent.Output) -> Unit) -> NotificationsTabComponent,
) : MainComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        mainComponentOutput: (MainComponent.Output) -> Unit,
    ) : this(
        componentContext = componentContext,
        mainComponentOutput = mainComponentOutput,
        homeTabComponent = { childContext, componentOutput ->
            HomeTabComponentDefault(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = componentOutput,
            )
        },
        browseTabComponent = { childContext, componentOutput ->
            BrowseTabComponentDefault(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = componentOutput,
            )
        },
        editorTabComponent = { childContext, componentOutput ->
            EditorTabComponentDefault(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = componentOutput,
            )
        },
        feedsTabComponent = { childContext, componentOutput ->
            FeedsTabComponentDefault(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = componentOutput,
            )
        },
        notificationsTabComponent = { childContext, componentOutput ->
            NotificationsTabComponentDefault(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = componentOutput,
            )
        },
    )

    private val navigation: StackNavigation<Config> = StackNavigation()

    private val stack: Value<ChildStack<Config, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Home,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override val childStack: Value<ChildStack<*, Child>> = stack

    override fun onHomeTabClicked() {
        navigation.bringToFront(Config.Home)
    }

    override fun onBrowseTabClicked() {
        navigation.bringToFront(Config.Browse)
    }

    override fun onEditorTabClicked() {
        navigation.bringToFront(Config.Editor)
    }

    override fun onFeedsTabClicked() {
        navigation.bringToFront(Config.Feeds)
    }

    override fun onNotificationsTabClicked() {
        navigation.bringToFront(Config.Notifications)
    }

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.Home -> Child.TabHome(homeTabComponent(componentContext, ::onHomeTabOutput))
            is Config.Browse -> Child.TabBrowse(browseTabComponent(componentContext, ::onBrowseTabOutput))
            is Config.Editor -> Child.TabEditor(editorTabComponent(componentContext, ::onEditorTabOutput))
            is Config.Feeds -> Child.TabFeeds(feedsTabComponent(componentContext, ::onFeedsTabOutput))
            is Config.Notifications -> Child.TabNotifications(notificationsTabComponent(componentContext, ::onNotificationsTabOutput))
        }

    private fun onHomeTabOutput(output: HomeTabComponent.Output) {
        when (output) {
            is HomeTabComponent.Output.ErrorCaught ->
                mainComponentOutput(MainComponent.Output.ErrorCaught(output.throwable))
        }
    }

    private fun onBrowseTabOutput(output: BrowseTabComponent.Output) {
        when (output) {
            is BrowseTabComponent.Output.ErrorCaught ->
                mainComponentOutput(MainComponent.Output.ErrorCaught(output.throwable))
        }
    }

    private fun onEditorTabOutput(output: EditorTabComponent.Output) {
        when (output) {
            is EditorTabComponent.Output.ErrorCaught ->
                mainComponentOutput(MainComponent.Output.ErrorCaught(output.throwable))
        }
    }

    private fun onFeedsTabOutput(output: FeedsTabComponent.Output) {
        when (output) {
            is FeedsTabComponent.Output.ErrorCaught ->
                mainComponentOutput(MainComponent.Output.ErrorCaught(output.throwable))
        }
    }

    private fun onNotificationsTabOutput(output: NotificationsTabComponent.Output) {
        when (output) {
            is NotificationsTabComponent.Output.ErrorCaught ->
                mainComponentOutput(MainComponent.Output.ErrorCaught(output.throwable))
        }
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object Home : Config

        @Serializable
        data object Browse : Config

        @Serializable
        data object Editor : Config

        @Serializable
        data object Feeds : Config

        @Serializable
        data object Notifications : Config
    }
}
