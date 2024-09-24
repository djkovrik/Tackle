package com.sedsoftware.tackle.editor.attachments.stubs

import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsGateways
import com.sedsoftware.tackle.editor.attachments.Responses
import com.sedsoftware.tackle.utils.test.BaseStub

class EditorAttachmentsApiStub : BaseStub(), EditorAttachmentsGateways.Api {

    var sendFileResponse: MediaAttachment = Responses.sendFileCorrectResponse

    override suspend fun sendFile(
        file: PlatformFileWrapper,
        onUpload: (Float) -> Unit,
        thumbnail: PlatformFileWrapper?,
        description: String?,
        focus: String?,
    ): MediaAttachment = asResponse(sendFileResponse)

}
