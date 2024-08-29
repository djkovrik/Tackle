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
import com.sedsoftware.tackle.explore.ExploreTabComponent
import com.sedsoftware.tackle.explore.integration.ExploreTabComponentDefault
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.home.integration.HomeTabComponentDefault
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.MainComponent.Child
import com.sedsoftware.tackle.main.model.TackleNavigationTab
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

    override fun onTabClicked(tab: TackleNavigationTab) {
        when (tab) {
            TackleNavigationTab.HOME -> navigation.bringToFront(Config.HomeTab)
            TackleNavigationTab.EXPLORE -> navigation.bringToFront(Config.ExploreTab)
            TackleNavigationTab.PUBLICATIONS -> navigation.bringToFront(Config.PublicationsTab)
            TackleNavigationTab.NOTIFICATIONS -> navigation.bringToFront(Config.NotificationsTab)
            TackleNavigationTab.PROFILE -> navigation.bringToFront(Config.ProfileTab)
        }
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
