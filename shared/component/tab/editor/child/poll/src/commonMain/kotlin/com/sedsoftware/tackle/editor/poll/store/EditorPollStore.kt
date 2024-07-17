package com.sedsoftware.tackle.editor.poll.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.poll.model.PollOption
import com.sedsoftware.tackle.editor.poll.store.EditorPollStore.Intent
import com.sedsoftware.tackle.editor.poll.store.EditorPollStore.State

interface EditorPollStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class OnRequestDurationPicker(val show: Boolean) : Intent()
        data class OnDurationSelected(val duration: PollDuration) : Intent()
        data class OnMultiselectEnabled(val enabled: Boolean) : Intent()
        data class OnTextInput(val id: String, val text: String) : Intent()
        data object OnAddPollOption : Intent()
        data class OnDeletePollOption(val id: String) : Intent()
        data class ChangePollState(val available: Boolean) : Intent()
        data class UpdateInstanceConfig(val config: Instance.Config) : Intent()
    }

    data class State(
        val config: Instance.Config = Instance.Config(),
        val configLoaded: Boolean = false,
        val options: List<PollOption> = emptyList(),
        val pollAvailable: Boolean = true,
        val multiselectEnabled: Boolean = false,
        val duration: PollDuration = PollDuration.NOT_SELECTED,
        val availableDurations: List<PollDuration> = emptyList(),
        val durationPickerVisible: Boolean = false,
        val insertionAvailable: Boolean = false,
        val deletionAvailable: Boolean = false,
    )
}
