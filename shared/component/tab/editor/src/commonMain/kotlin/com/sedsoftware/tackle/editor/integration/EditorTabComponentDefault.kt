package com.sedsoftware.tackle.editor.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.EditorTabComponent.Model
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.integration.EditorAttachmentsComponentDefault
import com.sedsoftware.tackle.editor.domain.EditorTabManager
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.emojis.integration.EditorEmojisComponentDefault
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.header.integration.EditorHeaderComponentDefault
import com.sedsoftware.tackle.editor.integration.attachments.EditorAttachmentsComponentApi
import com.sedsoftware.tackle.editor.integration.emojis.EditorEmojisComponentApi
import com.sedsoftware.tackle.editor.integration.emojis.EditorEmojisComponentDatabase
import com.sedsoftware.tackle.editor.integration.emojis.EditorEmojisComponentSettings
import com.sedsoftware.tackle.editor.integration.header.EditorHeaderComponentSettings
import com.sedsoftware.tackle.editor.integration.header.EditorHeaderComponentTools
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.poll.integration.EditorPollComponentDefault
import com.sedsoftware.tackle.editor.store.EditorTabStore
import com.sedsoftware.tackle.editor.store.EditorTabStore.Label
import com.sedsoftware.tackle.editor.store.EditorTabStoreProvider
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent
import com.sedsoftware.tackle.editor.warning.integration.EditorWarningComponentDefault
import com.sedsoftware.tackle.utils.extension.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class EditorTabComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: EditorTabComponentGateways.Api,
    private val database: EditorTabComponentGateways.Database,
    private val settings: EditorTabComponentGateways.Settings,
    private val tools: EditorTabComponentGateways.Tools,
    private val dispatchers: TackleDispatchers,
    private val editorOutput: (ComponentOutput) -> Unit,
) : EditorTabComponent, ComponentContext by componentContext {

    override val attachments: EditorAttachmentsComponent =
        EditorAttachmentsComponentDefault(
            componentContext = childContext(
                key = "Editor attachments",
                lifecycle = lifecycle
            ),
            storeFactory = storeFactory,
            api = EditorAttachmentsComponentApi(api),
            dispatchers = dispatchers,
            output = ::onChildOutput,
        )

    override val emojis: EditorEmojisComponent =
        EditorEmojisComponentDefault(
            componentContext = childContext(
                key = "Editor emojis",
                lifecycle = lifecycle
            ),
            storeFactory = storeFactory,
            api = EditorEmojisComponentApi(api),
            database = EditorEmojisComponentDatabase(database),
            settings = EditorEmojisComponentSettings(settings),
            dispatchers = dispatchers,
            output = ::onChildOutput,
        )

    override val header: EditorHeaderComponent =
        EditorHeaderComponentDefault(
            componentContext = childContext(
                key = "Editor header",
                lifecycle = lifecycle
            ),
            storeFactory = storeFactory,
            settings = EditorHeaderComponentSettings(settings),
            tools = EditorHeaderComponentTools(tools),
            dispatchers = dispatchers,
            output = ::onChildOutput,
        )

    override val poll: EditorPollComponent =
        EditorPollComponentDefault(
            componentContext = childContext(
                key = "Editor poll",
                lifecycle = lifecycle
            ),
            storeFactory = storeFactory,
            dispatchers = dispatchers,
        )

    override val warning: EditorWarningComponent =
        EditorWarningComponentDefault(
            componentContext = childContext(
                key = "Editor warning",
                lifecycle = lifecycle
            ),
            storeFactory = storeFactory,
            dispatchers = dispatchers,
        )

    private val store: EditorTabStore =
        instanceKeeper.getStore {
            EditorTabStoreProvider(
                storeFactory = storeFactory,
                manager = EditorTabManager(database),
                mainContext = dispatchers.main,
                ioContext = dispatchers.io,
            ).create()
        }

    init {
        val scope = CoroutineScope(dispatchers.main)

        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is Label.InstanceConfigLoaded -> {
                        attachments.updateInstanceConfig(label.config)
                        poll.updateInstanceConfig(label.config)
                    }

                    is Label.ErrorCaught -> {
                        editorOutput(ComponentOutput.Common.ErrorCaught(label.throwable))
                    }
                }
            }
        }

        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    override val model: Value<Model> = store.asValue().map(stateToModel)

    override fun onTextInput(text: String) {
        store.accept(EditorTabStore.Intent.OnTextInput(text))
    }

    override fun onEmojiSelected(emoji: CustomEmoji) {
        store.accept(EditorTabStore.Intent.OnEmojiSelect(emoji))
    }

    override fun onAttachmentsButtonClicked() {
        store.accept(EditorTabStore.Intent.OnAttachmentsButtonClick)
    }

    override fun onPollButtonClicked() {
        store.accept(EditorTabStore.Intent.OnPollButtonClick)
    }

    override fun onEmojisButtonClicked() {
        store.accept(EditorTabStore.Intent.OnEmojisButtonClick)
    }

    override fun onWarningButtonClicked() {
        store.accept(EditorTabStore.Intent.OnWarningButtonClick)
    }

    override fun onSendButtonClicked() {
        TODO("Status send not implemented yet")
    }

    private fun onChildOutput(output: ComponentOutput) {
        when (output) {
            is ComponentOutput.StatusEditor.EmojiSelected -> onEmojiSelected(output.emoji)
            else -> editorOutput(output)
        }
    }
}
