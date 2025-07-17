package com.sedsoftware.tackle.root.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
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
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.editor.EditorComponent
import com.sedsoftware.tackle.editor.integration.EditorComponentDefault
import com.sedsoftware.tackle.main.MainComponent
import com.sedsoftware.tackle.main.alternatetext.AlternateTextComponent
import com.sedsoftware.tackle.main.alternatetext.integration.AlternateTextComponentDefault
import com.sedsoftware.tackle.main.integration.MainComponentDefault
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponent
import com.sedsoftware.tackle.main.viewmedia.integration.ViewMediaComponentDefault
import com.sedsoftware.tackle.root.RootComponent
import com.sedsoftware.tackle.root.RootComponent.Child
import com.sedsoftware.tackle.root.gateway.auth.AuthComponentApi
import com.sedsoftware.tackle.root.gateway.auth.AuthComponentDatabase
import com.sedsoftware.tackle.root.gateway.auth.AuthComponentSettings
import com.sedsoftware.tackle.root.gateway.auth.AuthComponentTools
import com.sedsoftware.tackle.root.gateway.editor.EditorTabComponentApi
import com.sedsoftware.tackle.root.gateway.editor.EditorTabComponentDatabase
import com.sedsoftware.tackle.root.gateway.editor.EditorTabComponentSettings
import com.sedsoftware.tackle.root.gateway.editor.EditorTabComponentTools
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
    private val viewMediaComponent: (ComponentContext, List<MediaAttachment>, Int, () -> Unit) -> ViewMediaComponent,
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
                authorizedApi = authorizedApi,
                settings = settings,
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
        viewMediaComponent = { childContext, attachments, index, onBackClicked ->
            ViewMediaComponentDefault(
                componentContext = childContext,
                attachments = attachments,
                selectedIndex = index,
                onBackClicked = onBackClicked
            )
        },
    )

    private val scope: CoroutineScope = CoroutineScope(dispatchers.main)

    init {
        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    private val childStackNavigation: StackNavigation<Config> =
        StackNavigation()

    private val alternateTextSlotNavigation: SlotNavigation<AlternateTextConfig> =
        SlotNavigation<AlternateTextConfig>()

    private val stack: Value<ChildStack<Config, Child>> =
        childStack(
            source = childStackNavigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Auth,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    override val childStack: Value<ChildStack<*, Child>> = stack

    override val alternateTextDialog: Value<ChildSlot<*, AlternateTextComponent>> =
        childSlot(
            source = alternateTextSlotNavigation,
            serializer = AlternateTextConfig.serializer(),
            handleBackButton = true,
        ) { config, childComponentContext ->
            AlternateTextComponentDefault(
                componentContext = childComponentContext,
                text = config.text,
                onDismissed = alternateTextSlotNavigation::dismiss,
            )
        }
    private val exceptionHandler: TackleExceptionHandler =
        TackleExceptionHandler(
            logoutAction = { childStackNavigation.replaceCurrent(Config.Auth) }
        )

    override val errorMessages: Flow<TackleException>
        get() = exceptionHandler.messaging

    override fun onBack() {
        childStackNavigation.pop()
    }

    private fun createChild(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            is Config.Auth ->
                Child.Auth(authComponent(componentContext, ::onComponentOutput))

            is Config.Main ->
                Child.Main(mainComponent(componentContext, ::onComponentOutput))

            is Config.Editor ->
                Child.Editor(editorComponent(componentContext, ::onComponentOutput))

            is Config.ViewImage ->
                Child.ViewImage(viewMediaComponent(componentContext, config.attachments, config.index, childStackNavigation::pop))

            is Config.ViewVideo ->
                Child.ViewVideo(viewMediaComponent(componentContext, listOf(config.attachment), 0, childStackNavigation::pop))
        }

    private fun onComponentOutput(output: ComponentOutput) {
        when (output) {
            is ComponentOutput.Auth.AuthFlowCompleted -> {
                childStackNavigation.replaceCurrent(Config.Main)
            }

            is ComponentOutput.HomeTab.EditorRequested -> {
                childStackNavigation.pushNew(Config.Editor)
            }

            is ComponentOutput.HomeTab.ScheduledStatusesRequested -> {
                TODO("Open scheduled statuses")
            }

            is ComponentOutput.StatusEditor.BackButtonClicked -> {
                childStackNavigation.pop()
            }

            is ComponentOutput.StatusEditor.StatusPublished -> {
                childStackNavigation.pop()
                (stack.active.instance as? Child.Main)?.component?.showCreatedStatus(output.status)
            }

            is ComponentOutput.StatusEditor.ScheduledStatusPublished -> {
                childStackNavigation.pop()
            }

            is ComponentOutput.Common.ErrorCaught -> {
                exceptionHandler.consume(output.throwable, scope)
            }

            is ComponentOutput.SingleStatus.AlternateTextClicked -> {
                alternateTextSlotNavigation.activate(AlternateTextConfig(text = output.text))
            }

            is ComponentOutput.SingleStatus.ViewImage -> {
                childStackNavigation.pushNew(Config.ViewImage(output.attachments, output.selectedIndex))
            }

            is ComponentOutput.SingleStatus.ViewVideo -> {
                childStackNavigation.pushNew(Config.ViewVideo(output.attachment))
            }

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
        data object Editor : Config

        @Serializable
        data class ViewImage(val attachments: List<MediaAttachment>, val index: Int) : Config

        @Serializable
        data class ViewVideo(val attachment: MediaAttachment) : Config
    }

    @Serializable
    private data class AlternateTextConfig(
        val text: String,
    )
}
