package com.sedsoftware.tackle.editor.poll

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.poll.model.PollChoiceOption

interface EditorPollComponent {

    val model: Value<Model>

    fun onDurationPickerRequested(show: Boolean)
    fun onDurationSelected(duration: PollDuration)
    fun onMultiselectEnabled(enabled: Boolean)
    fun onHideTotalsEnabled(enabled: Boolean)
    fun onTextInput(id: String, text: String)
    fun onAddPollOptionClick()
    fun onDeletePollOptionClick(id: String)
    fun changeComponentAvailability(available: Boolean)
    fun toggleComponentVisibility()
    fun updateInstanceConfig(config: Instance.Config)
    fun resetComponentState()

    data class Model(
        val options: List<PollChoiceOption>,
        val multiselectEnabled: Boolean,
        val hideTotalsEnabled: Boolean,
        val duration: PollDuration,
        val availableDurations: List<PollDuration>,
        val durationPickerVisible: Boolean,
        val insertionAvailable: Boolean,
        val deletionAvailable: Boolean,
        val pollButtonAvailable: Boolean,
        val pollContentVisible: Boolean,
    )
}
