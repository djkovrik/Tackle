package com.sedsoftware.tackle.editor.poll.integration

import com.sedsoftware.tackle.editor.poll.EditorPollComponent.Model
import com.sedsoftware.tackle.editor.poll.store.EditorPollStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        options = it.options,
        multiselectEnabled = it.multiselectEnabled,
        hideTotalsEnabled = it.hideTotalsEnabled,
        duration = it.duration,
        availableDurations = it.availableDurations,
        durationPickerVisible = it.durationPickerVisible,
        insertionAvailable = it.insertionAvailable,
        deletionAvailable = it.deletionAvailable,
        pollButtonAvailable = it.pollAvailable,
        pollContentVisible = it.pollVisible,
    )
}
