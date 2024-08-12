package com.sedsoftware.tackle.editor.integration

import com.sedsoftware.tackle.editor.EditorTabComponent.Model
import com.sedsoftware.tackle.editor.store.EditorTabStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        statusText = it.statusText,
        statusTextSelection = it.statusTextSelection,
        statusCharactersLeft = it.statusCharactersLeft,
        suggestions = it.suggestions,
        datePickerVisible = it.datePickerVisible,
        scheduledDate = it.scheduledDate,
        timePickerVisible = it.timePickerVisible,
        scheduledHour = it.scheduledHour,
        scheduledMinute = it.scheduledMinute,
        scheduledIn24hrFormat = it.scheduledIn24hFormat,
    )
}
