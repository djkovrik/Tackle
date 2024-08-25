package com.sedsoftware.tackle.main.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.api.TackleDatabase
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.api.TacklePlatformTools
import com.sedsoftware.tackle.domain.api.TackleSettings
import com.sedsoftware.tackle.domain.api.UnauthorizedApi
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.integration.EditorTabComponentDefault
import com.sedsoftware.tackle.explore.ExploreTabComponent
import com.sedsoftware.tackle.explore.integration.ExploreTabComponentDefault
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.home.integration.HomeTabComponentDefault
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.MainComponent.Child
import com.sedsoftware.tackle.main.integration.editor.EditorTabComponentApi
import com.sedsoftware.tackle.main.integration.editor.EditorTabComponentDatabase
import com.sedsoftware.tackle.main.integration.editor.EditorTabComponentSettings
import com.sedsoftware.tackle.main.integration.editor.EditorTabComponentTools
import com.sedsoftware.tackle.main.model.TackleNavigationTab
import com.sedsoftware.tackle.notifications.NotificationsTabComponent
import com.sedsoftware.tackle.notifications.integration.NotificationsTabComponentDefault
import com.sedsoftware.tackle.publications.PublicationsTabComponent
import com.sedsoftware.tackle.publications.integration.PublicationsTabComponentDefault
import kotlinx.serialization.Serializable

class MainComponentDefault internal constructor(
    private val componentContext: ComponentContext,
    private val mainComponentOutput: (ComponentOutput) -> Unit,
    private val homeTabComponent: (ComponentContext, (ComponentOutput) -> Unit) -> HomeTabComponent,
    private val exploreTabComponent: (ComponentContext, (ComponentOutput) -> Unit) -> ExploreTabComponent,
    private val editorTabComponent: (ComponentContext, (ComponentOutput) -> Unit) -> EditorTabComponent,
    private val publicationsTabComponent: (ComponentContext, (ComponentOutput) -> Unit) -> PublicationsTabComponent,
    private val notificationsTabComponent: (ComponentContext, (ComponentOutput) -> Unit) -> NotificationsTabComponent,
) : MainComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        unauthorizedApi: UnauthorizedApi,
        authorizedApi: AuthorizedApi,
        database: TackleDatabase,
        settings: TackleSettings,
        platformTools: TacklePlatformTools,
        dispatchers: TackleDispatchers,
        mainComponentOutput: (ComponentOutput) -> Unit,
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
        exploreTabComponent = { childContext, componentOutput ->
            ExploreTabComponentDefault(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = componentOutput,
            )
        },
        editorTabComponent = { childContext, componentOutput ->
            EditorTabComponentDefault(
                componentContext = childContext,
                storeFactory = storeFactory,
                api = EditorTabComponentApi(unauthorizedApi, authorizedApi),
                database = EditorTabComponentDatabase(database),
                settings = EditorTabComponentSettings(settings),
                tools = EditorTabComponentTools(platformTools),
                dispatchers = dispatchers,
                editorOutput = componentOutput,
            )
        },
        publicationsTabComponent = { childContext, componentOutput ->
            PublicationsTabComponentDefault(
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
            initialConfiguration = Config.Editor,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override val childStack: Value<ChildStack<*, Child>> = stack

    override fun onTabClicked(tab: TackleNavigationTab) {
        when (tab) {
            TackleNavigationTab.HOME -> navigation.bringToFront(Config.Home)
            TackleNavigationTab.EXPLORE -> navigation.bringToFront(Config.Explore)
            TackleNavigationTab.EDITOR -> navigation.bringToFront(Config.Editor)
            TackleNavigationTab.PUBLICATIONS -> navigation.bringToFront(Config.Publications)
            TackleNavigationTab.NOTIFICATIONS -> navigation.bringToFront(Config.Notifications)
        }
    }

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.Home -> Child.TabHome(homeTabComponent(componentContext, ::onTabsOutput))
            is Config.Explore -> Child.TabExplore(exploreTabComponent(componentContext, ::onTabsOutput))
            is Config.Editor -> Child.TabEditor(editorTabComponent(componentContext, ::onTabsOutput))
            is Config.Publications -> Child.TabPublications(publicationsTabComponent(componentContext, ::onTabsOutput))
            is Config.Notifications -> Child.TabNotifications(notificationsTabComponent(componentContext, ::onTabsOutput))
        }

    private fun onTabsOutput(output: ComponentOutput) {
        when (output) {
            is ComponentOutput.StatusEditor.StatusPublished -> navigation.bringToFront(Config.Home)
            is ComponentOutput.StatusEditor.ScheduledStatusPublished -> navigation.bringToFront(Config.Home)
            else -> mainComponentOutput(output)
        }
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object Home : Config

        @Serializable
        data object Explore : Config

        @Serializable
        data object Editor : Config

        @Serializable
        data object Publications : Config

        @Serializable
        data object Notifications : Config
    }
}
