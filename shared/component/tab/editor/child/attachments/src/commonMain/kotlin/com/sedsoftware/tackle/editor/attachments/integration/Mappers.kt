package com.sedsoftware.tackle.editor.attachments.integration

import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent.Model
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        attachments = it.selectedFiles,
        attachmentButtonAvailable = it.configLoaded && it.attachmentsAvailable && !it.attachmentsAtLimit && !it.hasUploadInProgress,
    )
}
