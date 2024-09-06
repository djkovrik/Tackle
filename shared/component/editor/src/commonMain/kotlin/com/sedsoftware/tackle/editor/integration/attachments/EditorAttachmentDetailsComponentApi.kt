package com.sedsoftware.tackle.editor.integration.attachments

import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.editor.EditorComponentGateways
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsGateways.Api

class EditorAttachmentDetailsComponentApi(
    private val api: EditorComponentGateways.Api,
) : Api {

    override suspend fun updateFile(id: String, description: String?, focus: String?): MediaAttachment =
        api.updateFile(id, description, focus)
}
