package com.sedsoftware.tackle.editor.integration.attachments

import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsGateways

internal class EditorAttachmentsComponentApi(
    private val api: EditorTabComponentGateways.Api,
) : EditorAttachmentsGateways.Api {

    override suspend fun sendFile(
        file: PlatformFileWrapper,
        onUpload: (Int) -> Unit,
        thumbnail: PlatformFileWrapper?,
        description: String?,
        focus: String?
    ): MediaAttachment = api.sendFile(file, onUpload, thumbnail, description)
}
