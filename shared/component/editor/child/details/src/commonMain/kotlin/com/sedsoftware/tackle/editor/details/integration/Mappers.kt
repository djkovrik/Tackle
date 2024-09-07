package com.sedsoftware.tackle.editor.details.integration

import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent.Model
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        type = it.type,
        url = it.url,
        description = it.description,
        imageParams = it.imageParams,
        focus = it.focus,
        updatingAvailable = it.updatingAvailable,
        sending = it.sending,
    )
}
