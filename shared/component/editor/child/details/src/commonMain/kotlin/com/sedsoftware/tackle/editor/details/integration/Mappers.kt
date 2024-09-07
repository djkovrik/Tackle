package com.sedsoftware.tackle.editor.details.integration

import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent.Model
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        type = it.type,
        url = it.url,
        description = it.description,
        params = it.params,
        focus = it.focus,
        dataChanged = it.dataChanged,
        loading = it.loading,
    )
}
