package com.sedsoftware.tackle.editor.warning.integration

import com.sedsoftware.tackle.editor.warning.EditorWarningComponent.Model
import com.sedsoftware.tackle.editor.warning.store.EditorWarningStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        text = it.text
    )
}
