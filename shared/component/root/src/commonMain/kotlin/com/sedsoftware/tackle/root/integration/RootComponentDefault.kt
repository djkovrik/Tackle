package com.sedsoftware.tackle.root.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.integration.AuthComponentDefault
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.api.OAuthApi
import com.sedsoftware.tackle.domain.api.TackleDatabase
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.api.TacklePlatformTools
import com.sedsoftware.tackle.domain.api.TackleSettings
import com.sedsoftware.tackle.domain.api.UnauthorizedApi
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.integration.MainComponentDefault
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponent.Child
import com.sedsoftware.tackle.root.integration.auth.AuthComponentApi
import com.sedsoftware.tackle.root.integration.auth.AuthComponentSettings
import com.sedsoftware.tackle.root.integration.auth.AuthComponentTools
import kotlinx.serialization.Serializable

class RootComponentDefault internal constructor(
    componentContext: ComponentContext,
    private val authComponent: (ComponentContext, (ComponentOutput) -> Unit) -> AuthComponent,
    private val mainComponent: (ComponentContext, (ComponentOutput) -> Unit) -> MainComponent,
) : RootComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        unauthorizedApi: UnauthorizedApi,
        authorizedApi: AuthorizedApi,
        oauthApi: OAuthApi,
        database: TackleDatabase,
        settings: TackleSettings,
        platformTools: TacklePlatformTools,
        dispatchers: TackleDispatchers,
    ) : this(
        componentContext = componentContext,
        authComponent = { childContext, output ->
            AuthComponentDefault(
                componentContext = childContext,
                storeFactory = storeFactory,
                api = AuthComponentApi(unauthorizedApi, authorizedApi, oauthApi),
                settings = AuthComponentSettings(settings),
                tools = AuthComponentTools(platformTools),
                dispatchers = dispatchers,
                output = output,
            )
        },
        mainComponent = { childContext, output ->
            MainComponentDefault(
                componentContext = childContext,
                storeFactory = storeFactory,
                unauthorizedApi = unauthorizedApi,
                authorizedApi = authorizedApi,
                settings = settings,
                database = database,
                platformTools = platformTools,
                dispatchers = dispatchers,
                mainComponentOutput = output,
            )
        },
    )

    private val navigation: StackNavigation<Config> = StackNavigation()

    private val stack: Value<ChildStack<Config, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Auth,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override val childStack: Value<ChildStack<*, Child>> = stack

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.Auth -> Child.Auth(authComponent(componentContext, ::onComponentOutput))
            is Config.Main -> Child.Main(mainComponent(componentContext, ::onComponentOutput))
        }

    private fun onComponentOutput(output: ComponentOutput) {
        when (output) {
            is ComponentOutput.Auth.NavigateToMainScreen -> navigation.replaceCurrent(Config.Main)
            is ComponentOutput.Common.ErrorCaught -> Unit // TODO
            else -> Unit
        }
    }


    @Serializable
    private sealed interface Config {

        @Serializable
        data object Auth : Config

        @Serializable
        data object Main : Config
    }
}
