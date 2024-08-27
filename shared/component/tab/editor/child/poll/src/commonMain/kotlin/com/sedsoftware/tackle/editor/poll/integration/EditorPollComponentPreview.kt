package com.sedsoftware.tackle.editor.poll.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Instance.Config
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.poll.EditorPollComponent.Model
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.poll.model.PollChoiceOption

class EditorPollComponentPreview(
    options: List<PollChoiceOption> = emptyList(),
    multiselectEnabled: Boolean = false,
    duration: PollDuration = PollDuration.FIVE_MINUTES,
    availableDurations: List<PollDuration> = emptyList(),
    durationPickerVisible: Boolean = false,
    insertionAvailable: Boolean = false,
    deletionAvailable: Boolean = false,
    pollButtonAvailable: Boolean = true,
    pollContentVisible: Boolean = false,
) : EditorPollComponent {

    override val model: Value<Model> =
        MutableValue(
            Model(
                options = options,
                multiselectEnabled = multiselectEnabled,
                hideTotalsEnabled = false,
                duration = duration,
                availableDurations = availableDurations,
                durationPickerVisible = durationPickerVisible,
                insertionAvailable = insertionAvailable,
                deletionAvailable = deletionAvailable,
                pollButtonAvailable = pollButtonAvailable,
                pollContentVisible = pollContentVisible,
            )
        )

    override fun onDurationPickerRequested(show: Boolean) = Unit
    override fun onDurationSelected(duration: PollDuration) = Unit
    override fun onMultiselectEnabled(enabled: Boolean) = Unit
    override fun onHideTotalsEnabled(enabled: Boolean) = Unit
    override fun onTextInput(id: String, text: String) = Unit
    override fun onAddPollOptionClick() = Unit
    override fun onDeletePollOptionClick(id: String) = Unit
    override fun changeComponentAvailability(available: Boolean) = Unit
    override fun toggleComponentVisibility() = Unit
    override fun updateInstanceConfig(config: Config) = Unit
    override fun resetComponentState() = Unit
}
