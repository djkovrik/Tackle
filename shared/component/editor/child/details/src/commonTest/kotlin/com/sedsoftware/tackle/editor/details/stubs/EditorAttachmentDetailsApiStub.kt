package com.sedsoftware.tackle.editor.details.stubs

import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsGateways
import com.sedsoftware.tackle.utils.test.BaseStub

class EditorAttachmentDetailsApiStub : BaseStub(), EditorAttachmentDetailsGateways.Api {

    var response: MediaAttachment = EditorAttachmentDetailsApiStubResponses.basicResponse

    override suspend fun updateFile(id: String, description: String?, focus: String?): MediaAttachment =
        asResponse(response)

    override suspend fun getFile(id: String): MediaAttachment =
        asResponse(response)
}
