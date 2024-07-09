package com.sedsoftware.tackle.editor.attachments.stubs

import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsGateways
import com.sedsoftware.tackle.utils.test.StubWithException

class EditorAttachmentsApiStub : StubWithException(), EditorAttachmentsGateways.Api {

    companion object {
        val sendFileCorrectResponse: MediaAttachment =
            MediaAttachment(
                id = "123456",
                type = MediaAttachmentType.IMAGE,
                url = "http://some.url",
                previewUrl = "http://some.url/preview",
                remoteUrl = "http://some.url/remote",
                description = "Some description",
                blurhash = "abcd",
                meta = null,
            )
    }

    var sendFileResponse: MediaAttachment = sendFileCorrectResponse

    override suspend fun sendFile(
        file: PlatformFileWrapper,
        onUpload: (Int) -> Unit,
        thumbnail: PlatformFileWrapper?,
        description: String?,
        focus: String?
    ): MediaAttachment {
        return asResponse(sendFileResponse)
    }
}
