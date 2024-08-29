package com.sedsoftware.tackle.editor.poll.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.poll.domain.EditorPollManager
import com.sedsoftware.tackle.editor.poll.extension.applyInput
import com.sedsoftware.tackle.editor.poll.model.PollChoiceOption
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.poll.store.EditorPollStore.Intent
import com.sedsoftware.tackle.editor.poll.store.EditorPollStore.State
import com.sedsoftware.tackle.domain.StoreCreate
import com.sedsoftware.tackle.utils.generateUUID
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class EditorPollStoreProvider(
    private val storeFactory: StoreFactory,
    private val manager: EditorPollManager,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
) {

    private val emptyPollOption: PollChoiceOption
        get() = PollChoiceOption(id = generateUUID(), text = "")

    @StoreCreate
    fun create(autoInit: Boolean = true): EditorPollStore =
        object : EditorPollStore, Store<Intent, State, Nothing> by storeFactory.create<Intent, Action, Msg, State, Nothing>(
            name = "EditorPollStore",
            initialState = State(),
            autoInit = autoInit,
            executorFactory = coroutineExecutorFactory(mainContext) {
                onAction<Action.RefreshAvailableDurations> {
                    launch {
                        val durations = withContext(ioContext) { manager.getAvailableDurations(state().config) }
                        dispatch(Msg.DurationsAvailable(durations))
                    }
                }

                onIntent<Intent.UpdateInstanceConfig> {
                    dispatch(Msg.InstanceConfigAvailable(it.config))
                    forward(Action.RefreshAvailableDurations)
                }

                onIntent<Intent.OnRequestDurationPicker> { dispatch(Msg.DurationDialogVisibilityChanged(it.show)) }

                onIntent<Intent.OnDurationSelected> { dispatch(Msg.DurationSelected(it.duration)) }

                onIntent<Intent.OnMultiselectEnabled> { dispatch(Msg.MultiselectEnabled(it.enabled)) }

                onIntent<Intent.OnHideTotalsEnabled> { dispatch(Msg.HideTotalsEnabled(it.enabled)) }

                onIntent<Intent.ChangeComponentAvailability> { dispatch(Msg.ComponentAvailabilityChanged(it.available)) }

                onIntent<Intent.ToggleComponentVisibility> { dispatch(Msg.ComponentVisibilityToggled) }

                onIntent<Intent.OnTextInput> { dispatch(Msg.TextInput(it.id, it.text)) }

                onIntent<Intent.OnAddPollOption> { dispatch(Msg.PollOptionAdded) }

                onIntent<Intent.OnDeletePollOption> { dispatch(Msg.PollOptionDeleted(it.id)) }

                onIntent<Intent.ResetState> { dispatch(Msg.StateReset) }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.InstanceConfigAvailable -> copy(
                        config = msg.config,
                        configLoaded = true,
                        options = listOf(emptyPollOption, emptyPollOption),
                        insertionAvailable = true,
                        deletionAvailable = false,
                        maxTextOptionLength = msg.config.polls.maxCharactersPerOption,
                    )

                    is Msg.DurationsAvailable -> copy(
                        availableDurations = msg.durations,
                        duration = msg.durations.first(),
                    )

                    is Msg.DurationDialogVisibilityChanged -> copy(
                        durationPickerVisible = msg.visible,
                    )

                    is Msg.DurationSelected -> copy(
                        duration = msg.duration,
                    )

                    is Msg.MultiselectEnabled -> copy(
                        multiselectEnabled = msg.enabled,
                    )

                    is Msg.HideTotalsEnabled -> copy(
                        hideTotalsEnabled = msg.enabled,
                    )

                    is Msg.ComponentAvailabilityChanged -> copy(
                        pollAvailable = msg.available,
                    )

                    is Msg.ComponentVisibilityToggled -> copy(
                        pollVisible = !pollVisible,
                    )

                    is Msg.TextInput -> copy(
                        options = options.applyInput(msg.id, msg.text.take(maxTextOptionLength)),
                    )

                    is Msg.PollOptionAdded -> copy(
                        options = options + listOf(emptyPollOption),
                        insertionAvailable = options.size + 1 < config.polls.maxOptions,
                        deletionAvailable = true,
                    )

                    is Msg.PollOptionDeleted -> copy(
                        options = options.filterNot { it.id == msg.id },
                        deletionAvailable = options.size - 1 > MINIMUM_POLL_OPTIONS,
                        insertionAvailable = true,
                    )

                    is Msg.StateReset -> copy(
                        options = listOf(emptyPollOption, emptyPollOption),
                        insertionAvailable = true,
                        deletionAvailable = false,
                        pollAvailable = true,
                        pollVisible = false,
                        multiselectEnabled = false,
                        hideTotalsEnabled = false,
                        durationPickerVisible = false,
                        duration = availableDurations.first(),
                    )
                }
            },
        ) {}

    sealed interface Action {
        data object RefreshAvailableDurations : Action
    }

    sealed interface Msg {
        data class InstanceConfigAvailable(val config: Instance.Config) : Msg
        data class DurationsAvailable(val durations: List<PollDuration>) : Msg
        data class DurationDialogVisibilityChanged(val visible: Boolean) : Msg
        data class DurationSelected(val duration: PollDuration) : Msg
        data class MultiselectEnabled(val enabled: Boolean) : Msg
        data class HideTotalsEnabled(val enabled: Boolean) : Msg
        data class ComponentAvailabilityChanged(val available: Boolean) : Msg
        data object ComponentVisibilityToggled : Msg
        data class TextInput(val id: String, val text: String) : Msg
        data object PollOptionAdded : Msg
        data class PollOptionDeleted(val id: String) : Msg
        data object StateReset : Msg
    }

    private companion object {
        const val MINIMUM_POLL_OPTIONS = 2
    }
}
