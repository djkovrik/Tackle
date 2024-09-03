package com.sedsoftware.tackle.root.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.auth.AuthComponent
import com.sedsoftware.tackle.auth.integration.AuthComponentDefault
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.domain.TackleExceptionHandler
import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.api.OAuthApi
import com.sedsoftware.tackle.domain.api.TackleDatabase
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.api.TacklePlatformTools
import com.sedsoftware.tackle.domain.api.TackleSettings
import com.sedsoftware.tackle.domain.api.UnauthorizedApi
import com.sedsoftware.tackle.editor.EditorComponent
import com.sedsoftware.tackle.editor.integration.EditorComponentDefault
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.integration.MainComponentDefault
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponent.Child
import com.sedsoftware.tackle.root.integration.auth.AuthComponentApi
import com.sedsoftware.tackle.root.integration.auth.AuthComponentDatabase
import com.sedsoftware.tackle.root.integration.auth.AuthComponentSettings
import com.sedsoftware.tackle.root.integration.auth.AuthComponentTools
import com.sedsoftware.tackle.root.integration.editor.EditorTabComponentApi
import com.sedsoftware.tackle.root.integration.editor.EditorTabComponentDatabase
import com.sedsoftware.tackle.root.integration.editor.EditorTabComponentSettings
import com.sedsoftware.tackle.root.integration.editor.EditorTabComponentTools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

class RootComponentDefault internal constructor(
    componentContext: ComponentContext,
    dispatchers: TackleDispatchers,
    private val authComponent: (ComponentContext, (ComponentOutput) -> Unit) -> AuthComponent,
    private val mainComponent: (ComponentContext, (ComponentOutput) -> Unit) -> MainComponent,
    private val editorComponent: (ComponentContext, (ComponentOutput) -> Unit) -> EditorComponent,
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
        dispatchers = dispatchers,
        authComponent = { childContext, output ->
            AuthComponentDefault(
                componentContext = childContext,
                storeFactory = storeFactory,
                api = AuthComponentApi(unauthorizedApi, authorizedApi, oauthApi),
                database = AuthComponentDatabase(database),
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
        editorComponent = { childContext, componentOutput ->
            EditorComponentDefault(
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
    )

    private val scope: CoroutineScope = CoroutineScope(dispatchers.main)

    init {
        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

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

    private val exceptionHandler: TackleExceptionHandler =
        TackleExceptionHandler(
            logoutAction = { navigation.replaceCurrent(Config.Auth) }
        )

    override val errorMessages: Flow<TackleException>
        get() = exceptionHandler.messaging

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.Auth -> Child.Auth(authComponent(componentContext, ::onComponentOutput))
            is Config.Main -> Child.Main(mainComponent(componentContext, ::onComponentOutput))
            is Config.Editor -> Child.Editor(editorComponent(componentContext, ::onComponentOutput))
        }

    private fun onComponentOutput(output: ComponentOutput) {
        when (output) {
            is ComponentOutput.Auth.AuthFlowCompleted -> navigation.replaceCurrent(Config.Main)
            is ComponentOutput.HomeTab.EditorRequested -> navigation.pushNew(Config.Editor)
            is ComponentOutput.StatusEditor.BackButtonClicked -> navigation.pop()
            is ComponentOutput.StatusEditor.StatusPublished -> navigation.pop()
            is ComponentOutput.StatusEditor.ScheduledStatusPublished -> navigation.pop()
            is ComponentOutput.Common.ErrorCaught -> exceptionHandler.consume(output.throwable, scope)
            else -> Unit
        }
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object Auth : Config

        @Serializable
        data object Main : Config

        @Serializable
        data object Editor: Config
    }
}
