package com.sedsoftware.tackle.editor.poll.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.poll.model.PollChoiceOption
import com.sedsoftware.tackle.editor.poll.store.EditorPollStore.Intent
import com.sedsoftware.tackle.editor.poll.store.EditorPollStore.State

interface EditorPollStore : Store<Intent, State, Nothing> {

    sealed interface Intent {
        data class OnDurationPickerRequested(val show: Boolean) : Intent
        data class OnDurationSelected(val duration: PollDuration) : Intent
        data class OnMultiselectEnabled(val enabled: Boolean) : Intent
        data class OnHideTotalsEnabled(val enabled: Boolean) : Intent
        data class OnTextInput(val id: String, val text: String) : Intent
        data object OnPollOptionAdded : Intent
        data class OnPollOptionDeleted(val id: String) : Intent
        data class ChangeComponentAvailability(val available: Boolean) : Intent
        data object ToggleComponentVisibility : Intent
        data class UpdateInstanceConfig(val config: Instance.Config) : Intent
    }

    data class State(
        val config: Instance.Config = Instance.Config(),
        val configLoaded: Boolean = false,
        val options: List<PollChoiceOption> = emptyList(),
        val pollAvailable: Boolean = true,
        val pollVisible: Boolean = false,
        val multiselectEnabled: Boolean = false,
        val hideTotalsEnabled: Boolean = false,
        val duration: PollDuration = PollDuration.NOT_SELECTED,
        val availableDurations: List<PollDuration> = emptyList(),
        val durationPickerVisible: Boolean = false,
        val insertionAvailable: Boolean = false,
        val deletionAvailable: Boolean = false,
        val maxTextOptionLength: Int = 0,
    )
}
