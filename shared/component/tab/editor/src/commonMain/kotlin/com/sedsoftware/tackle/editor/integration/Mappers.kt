package com.sedsoftware.tackle.editor.integration

import com.sedsoftware.tackle.editor.EditorTabComponent.Model
import com.sedsoftware.tackle.editor.store.EditorTabStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        statusText = it.statusText,
        statusCharactersLeft = it.statusCharactersLeft,
        attachmentsActive = it.attachmentsActive,
        emojisActive = it.emojisActive,
        pollActive = it.pollActive,
        warningActive = it.warningActive,
        sendingAvailable = it.sendingAvailable,
    )
}
