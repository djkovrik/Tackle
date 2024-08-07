package com.sedsoftware.tackle.editor.emojis.integration

import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent.Model
import com.sedsoftware.tackle.editor.emojis.store.EditorEmojisStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        emojis = it.emojis,
        emojisButtonAvailable = it.emojisAvailable,
        emojisContentVisible = it.emojisVisible,
    )
}
