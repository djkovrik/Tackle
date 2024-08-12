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
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
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
            componentContext = childContext(key = "Editor attachments"),
            storeFactory = storeFactory,
            api = EditorAttachmentsComponentApi(api),
            dispatchers = dispatchers,
            output = ::onChildOutput,
        )

    override val emojis: EditorEmojisComponent =
        EditorEmojisComponentDefault(
            componentContext = childContext(key = "Editor emojis"),
            storeFactory = storeFactory,
            api = EditorEmojisComponentApi(api),
            database = EditorEmojisComponentDatabase(database),
            settings = EditorEmojisComponentSettings(settings),
            dispatchers = dispatchers,
            output = ::onChildOutput,
        )

    override val header: EditorHeaderComponent =
        EditorHeaderComponentDefault(
            componentContext = childContext(key = "Editor header"),
            storeFactory = storeFactory,
            settings = EditorHeaderComponentSettings(settings),
            tools = EditorHeaderComponentTools(tools),
            dispatchers = dispatchers,
            output = ::onChildOutput,
        )

    override val poll: EditorPollComponent =
        EditorPollComponentDefault(
            componentContext = childContext(key = "Editor poll"),
            storeFactory = storeFactory,
            dispatchers = dispatchers,
        )

    override val warning: EditorWarningComponent =
        EditorWarningComponentDefault(
            componentContext = childContext(key = "Editor warning"),
            storeFactory = storeFactory,
            dispatchers = dispatchers,
        )

    private val store: EditorTabStore =
        instanceKeeper.getStore {
            EditorTabStoreProvider(
                storeFactory = storeFactory,
                manager = EditorTabManager(api, database, tools),
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

    override fun onTextInput(text: String, selection: Pair<Int, Int>) {
        store.accept(EditorTabStore.Intent.OnTextInput(text, selection))
    }

    override fun onEmojiSelected(emoji: CustomEmoji) {
        store.accept(EditorTabStore.Intent.OnEmojiSelect(emoji))
    }

    override fun onInputHintSelected(hint: EditorInputHintItem) {
        store.accept(EditorTabStore.Intent.OnInputHintSelect(hint))
    }

    override fun onPollButtonClicked() {
        val isPollVisibleNow = poll.model.value.pollContentVisible
        attachments.changeComponentAvailability(available = isPollVisibleNow)
        poll.toggleComponentVisibility()
    }

    override fun onEmojisButtonClicked() {
        emojis.toggleComponentVisibility()
    }

    override fun onWarningButtonClicked() {
        warning.toggleComponentVisibility()
    }

    override fun onScheduleDatePickerRequested(show: Boolean) {
        store.accept(EditorTabStore.Intent.OnRequestDatePicker(show))
    }

    override fun onScheduleDateSelected(millis: Long) {
        store.accept(EditorTabStore.Intent.OnScheduleDate(millis))
    }

    private fun onChildOutput(output: ComponentOutput) {
        when (output) {
            is ComponentOutput.StatusEditor.AttachmentsCountUpdated -> poll.changeComponentAvailability(available = output.count == 0)
            is ComponentOutput.StatusEditor.EmojiSelected -> onEmojiSelected(output.emoji)
            else -> editorOutput(output)
        }
    }
}
