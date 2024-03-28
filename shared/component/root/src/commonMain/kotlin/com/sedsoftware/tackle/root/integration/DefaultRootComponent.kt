package com.sedsoftware.tackle.root.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.integration.AuthComponentDefault
import com.sedsoftware.tackle.network.api.AuthorizedApi
import com.sedsoftware.tackle.network.api.UnauthorizedApi
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponent.Child
import com.sedsoftware.tackle.settings.api.TackleSettings
import com.sedsoftware.tackle.utils.TackleDispatchers
import kotlinx.serialization.Serializable

class DefaultRootComponent internal constructor(
    componentContext: ComponentContext,
    private val authComponent: (ComponentContext, (AuthComponent.Output) -> Unit) -> AuthComponent,
) : RootComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        unauthorizedApi: UnauthorizedApi,
        authorizedApi: AuthorizedApi,
        settings: TackleSettings,
        dispatchers: TackleDispatchers,
    ) : this(
        componentContext = componentContext,
        authComponent = { childContext, output ->
            AuthComponentDefault(
                componentContext = childContext,
                storeFactory = storeFactory,
                api = unauthorizedApi,
                dispatchers = dispatchers,
                output = output,
            )
        }
    )

    private val navigation: StackNavigation<Config> = StackNavigation()

    private val stack: Value<ChildStack<Config, Child>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.Auth,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override val childStack: Value<ChildStack<*, Child>> = stack

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.Auth -> Child.Auth(authComponent(componentContext, ::onAuthComponentOutput))
        }

    private fun onAuthComponentOutput(output: AuthComponent.Output) {
        // TODO Error handler
    }

    @Serializable
    private sealed interface Config : Parcelable {
        @Serializable
        data object Auth : Config
    }
}
