package com.sedsoftware.tackle.editor.details.integration

import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent.Model
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        description = it.description,
        focus = it.focus,
        updatingAvailable = it.updatingAvailable,
        sending = it.sending,
    )
}
