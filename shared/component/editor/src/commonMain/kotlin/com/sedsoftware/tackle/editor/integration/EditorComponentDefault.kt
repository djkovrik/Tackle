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
import com.sedsoftware.tackle.domain.model.NewStatusBundle
import com.sedsoftware.tackle.editor.EditorComponent
import com.sedsoftware.tackle.editor.EditorComponent.Model
import com.sedsoftware.tackle.editor.EditorComponentGateways
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.integration.EditorAttachmentsComponentDefault
import com.sedsoftware.tackle.editor.domain.EditorManager
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
import com.sedsoftware.tackle.editor.store.EditorStore
import com.sedsoftware.tackle.editor.store.EditorStore.Label
import com.sedsoftware.tackle.editor.store.EditorStoreProvider
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent
import com.sedsoftware.tackle.editor.warning.integration.EditorWarningComponentDefault
import com.sedsoftware.tackle.utils.extension.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class EditorComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: EditorComponentGateways.Api,
    private val database: EditorComponentGateways.Database,
    private val settings: EditorComponentGateways.Settings,
    private val tools: EditorComponentGateways.Tools,
    private val dispatchers: TackleDispatchers,
    private val editorOutput: (ComponentOutput) -> Unit,
) : EditorComponent, ComponentContext by componentContext {

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

    private val attachmentsModel: EditorAttachmentsComponent.Model get() = attachments.model.value
    private val headerModel: EditorHeaderComponent.Model get() = header.model.value
    private val pollModel: EditorPollComponent.Model get() = poll.model.value
    private val warningModel: EditorWarningComponent.Model get() = warning.model.value
    private val editorModel: EditorComponent.Model get() = model.value

    private val store: EditorStore =
        instanceKeeper.getStore {
            EditorStoreProvider(
                storeFactory = storeFactory,
                manager = EditorManager(api, database, tools),
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

                    is Label.TextUpdated -> {
                        val inputIsNotEmpty = label.text.isNotEmpty()
                        header.changeSendingAvailability(inputIsNotEmpty)
                    }

                    is Label.StatusSent -> {
                        editorOutput(ComponentOutput.StatusEditor.StatusPublished)
                    }

                    is Label.ScheduledStatusSent -> {
                        editorOutput(ComponentOutput.StatusEditor.ScheduledStatusPublished)
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
        store.accept(EditorStore.Intent.OnTextInput(text, selection))
    }

    override fun onEmojiSelected(emoji: CustomEmoji) {
        store.accept(EditorStore.Intent.OnEmojiSelect(emoji))
    }

    override fun onInputHintSelected(hint: EditorInputHintItem) {
        store.accept(EditorStore.Intent.OnInputHintSelect(hint))
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
        store.accept(EditorStore.Intent.OnRequestDatePicker(show))
    }

    override fun onScheduleDateSelected(millis: Long) {
        store.accept(EditorStore.Intent.OnScheduleDate(millis))
    }

    override fun onScheduleTimePickerRequested(show: Boolean) {
        store.accept(EditorStore.Intent.OnRequestTimePicker(show))
    }

    override fun onScheduleTimeSelected(hour: Int, minute: Int, formatIn24hr: Boolean) {
        store.accept(EditorStore.Intent.OnScheduleTime(hour, minute, formatIn24hr))
    }

    override fun resetScheduledDateTime() {
        store.accept(EditorStore.Intent.OnScheduledDateTimeReset)
    }

    override fun onSendButtonClicked() {
        val bundle: NewStatusBundle = NewStatusBundle.Builder().apply {
            applyStatus(this)
            applyLanguage(this)
            applyVisibility(this)
            applyMediaIds(this)
            applyPollOptions(this)
            applyIsSensitive(this)
            applyScheduledDateTime(this)
        }
            .build()

        store.accept(EditorStore.Intent.SendStatus(bundle))
    }

    override fun onBackButtonClicked() {
        editorOutput(ComponentOutput.StatusEditor.BackButtonClicked)
    }

    private fun applyStatus(builder: NewStatusBundle.Builder): NewStatusBundle.Builder {
        return if (editorModel.statusText.isNotEmpty()) {
            builder.status(editorModel.statusText)
        } else {
            builder
        }
    }

    private fun applyLanguage(builder: NewStatusBundle.Builder): NewStatusBundle.Builder {
        return builder.language(headerModel.selectedLocale.languageCode)
    }

    private fun applyVisibility(builder: NewStatusBundle.Builder): NewStatusBundle.Builder {
        return builder.visibility(headerModel.statusVisibility)
    }

    private fun applyMediaIds(builder: NewStatusBundle.Builder): NewStatusBundle.Builder {
        val uploadedFiles = attachmentsModel.attachments
            .filter { it.serverCopy != null && !it.serverCopy?.id.isNullOrEmpty() }
            .map { it.serverCopy?.id.orEmpty() }

        return if (uploadedFiles.isNotEmpty()) {
            builder.mediaIds(uploadedFiles)
        } else {
            builder
        }
    }

    private fun applyPollOptions(builder: NewStatusBundle.Builder): NewStatusBundle.Builder {
        return if (pollModel.pollContentVisible && pollModel.options.isNotEmpty()) {
            builder
                .pollOptions(pollModel.options.map { it.text })
                .pollExpiresIn(pollModel.duration.seconds)
                .pollAllowMultiple(pollModel.multiselectEnabled)
                .pollHideTotals(pollModel.hideTotalsEnabled)
        } else {
            builder
        }
    }

    private fun applyIsSensitive(builder: NewStatusBundle.Builder): NewStatusBundle.Builder {
        return if (warningModel.warningContentVisible && warningModel.text.isNotEmpty()) {
            builder
                .sensitive(true)
                .spoilerText(warningModel.text)
        } else {
            builder.sensitive(false)
        }
    }

    private fun applyScheduledDateTime(builder: NewStatusBundle.Builder): NewStatusBundle.Builder {
        return if (editorModel.scheduledDate > 0 && editorModel.scheduledHour >= 0 && editorModel.scheduledMinute >= 0) {
            builder
                .scheduledAtDate(editorModel.scheduledDate)
                .scheduledAtHour(editorModel.scheduledHour)
                .scheduledAtMinute(editorModel.scheduledMinute)
        } else {
            builder
        }
    }

    private fun onChildOutput(output: ComponentOutput) {
        when (output) {
            is ComponentOutput.StatusEditor.PendingAttachmentsCountUpdated -> {
                poll.changeComponentAvailability(available = output.count == 0)
            }

            is ComponentOutput.StatusEditor.LoadedAttachmentsCountUpdated -> {
                header.changeSendingAvailability(available = output.count != 0)
            }

            is ComponentOutput.StatusEditor.EmojiSelected -> {
                onEmojiSelected(output.emoji)
            }

            else -> {
                editorOutput(output)
            }
        }
    }
}
