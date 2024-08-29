package com.sedsoftware.tackle.editor.integration

import com.sedsoftware.tackle.editor.EditorComponent.Model
import com.sedsoftware.tackle.editor.store.EditorStore.State
import com.sedsoftware.tackle.utils.DateTimeUtils

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
        scheduledDateLabel = DateTimeUtils.getDisplayingLabelFromPickers(it.scheduledDate, it.scheduledHour, it.scheduledMinute)
    )
}
