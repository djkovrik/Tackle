package com.sedsoftware.tackle.editor.integration.attachments

import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.EditorComponentGateways
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsGateways

internal class EditorAttachmentsComponentApi(
    private val api: EditorComponentGateways.Api,
) : EditorAttachmentsGateways.Api {

    override suspend fun sendFile(
        file: PlatformFileWrapper,
        onUpload: (Float) -> Unit,
        thumbnail: PlatformFileWrapper?,
        description: String?,
        focus: String?,
    ): MediaAttachment = api.sendFile(file, onUpload, thumbnail, description)
}
