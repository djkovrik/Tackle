package com.sedsoftware.tackle.editor.integration

import com.sedsoftware.tackle.editor.EditorTabComponent.Model
import com.sedsoftware.tackle.editor.store.EditorTabStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        statusText = it.statusText,
        statusTextSelection = it.statusTextSelection,
        statusCharactersLeft = it.statusCharactersLeft,
        emojisVisible = it.emojisVisible,
        pollVisible = it.pollVisible,
        warningVisible = it.warningVisible,
        sendingAvailable = it.sendingAvailable,
    )
}
