package com.sedsoftware.tackle.editor.content.integration

import com.sedsoftware.tackle.editor.content.EditorEmojisComponent.Model
import com.sedsoftware.tackle.editor.content.store.EditorEmojisStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        emojis = it.emojis.filter { emoji -> emoji.visibleInPicker },
        emojiPickerAvailable = it.emojiPickerAvailable,
    )
}
