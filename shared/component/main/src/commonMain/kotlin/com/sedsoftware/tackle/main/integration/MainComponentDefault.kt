package com.sedsoftware.tackle.main.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.api.TacklePlatformTools
import com.sedsoftware.tackle.domain.api.TackleSettings
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.explore.ExploreTabComponent
import com.sedsoftware.tackle.explore.integration.ExploreTabComponentDefault
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.home.integration.HomeTabComponentDefault
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.MainComponent.Child
import com.sedsoftware.tackle.main.gateway.StatusComponentApi
import com.sedsoftware.tackle.main.gateway.StatusComponentSettings
import com.sedsoftware.tackle.main.gateway.StatusComponentTools
import com.sedsoftware.tackle.main.model.MainScreenTab
import com.sedsoftware.tackle.notifications.NotificationsTabComponent
import com.sedsoftware.tackle.notifications.integration.NotificationsTabComponentDefault
import com.sedsoftware.tackle.profile.ProfileTabComponent
import com.sedsoftware.tackle.profile.integration.ProfileTabComponentDefault
import com.sedsoftware.tackle.publications.PublicationsTabComponent
import com.sedsoftware.tackle.publications.integration.PublicationsTabComponentDefault
import kotlinx.serialization.Serializable

class MainComponentDefault internal constructor(
    private val componentContext: ComponentContext,
    private val mainComponentOutput: (ComponentOutput) -> Unit,
    private val homeTabComponent: (ComponentContext, (ComponentOutput) -> Unit) -> HomeTabComponent,
    private val exploreTabComponent: (ComponentContext, (ComponentOutput) -> Unit) -> ExploreTabComponent,
    private val publicationsTabComponent: (ComponentContext, (ComponentOutput) -> Unit) -> PublicationsTabComponent,
    private val notificationsTabComponent: (ComponentContext, (ComponentOutput) -> Unit) -> NotificationsTabComponent,
    private val profileTabComponent: (ComponentContext, (ComponentOutput) -> Unit) -> ProfileTabComponent,
) : MainComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        authorizedApi: AuthorizedApi,
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
                api = StatusComponentApi(authorizedApi),
                settings = StatusComponentSettings(settings),
                tools = StatusComponentTools(platformTools),
                dispatchers = dispatchers,
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
        publicationsTabComponent = { childContext, componentOutput ->
            PublicationsTabComponentDefault(
                componentContext = childContext,
                storeFactory = storeFactory,
                api = StatusComponentApi(authorizedApi),
                settings = StatusComponentSettings(settings),
                tools = StatusComponentTools(platformTools),
                dispatchers = dispatchers,
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
        profileTabComponent = { childContext, componentOutput ->
            ProfileTabComponentDefault(
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
            initialConfiguration = Config.HomeTab,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override val childStack: Value<ChildStack<*, Child>> = stack

    override fun onTabClick(tab: MainScreenTab) {
        when (tab) {
            MainScreenTab.HOME -> navigation.bringToFront(Config.HomeTab)
            MainScreenTab.EXPLORE -> navigation.bringToFront(Config.ExploreTab)
            MainScreenTab.PUBLICATIONS -> navigation.bringToFront(Config.PublicationsTab)
            MainScreenTab.NOTIFICATIONS -> navigation.bringToFront(Config.NotificationsTab)
            MainScreenTab.PROFILE -> navigation.bringToFront(Config.ProfileTab)
        }
    }

    override fun showCreatedStatus(status: Status) {
        (stack.active.instance as? Child.HomeTab)?.component?.showCreatedStatus(status)
    }

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.HomeTab -> Child.HomeTab(homeTabComponent(componentContext, mainComponentOutput))
            is Config.ExploreTab -> Child.ExploreTab(exploreTabComponent(componentContext, mainComponentOutput))
            is Config.PublicationsTab -> Child.PublicationsTab(publicationsTabComponent(componentContext, mainComponentOutput))
            is Config.NotificationsTab -> Child.NotificationsTab(notificationsTabComponent(componentContext, mainComponentOutput))
            is Config.ProfileTab -> Child.ProfileTab(profileTabComponent(componentContext, mainComponentOutput))
        }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object HomeTab : Config

        @Serializable
        data object ExploreTab : Config

        @Serializable
        data object PublicationsTab : Config

        @Serializable
        data object NotificationsTab : Config

        @Serializable
        data object ProfileTab : Config
    }
}
