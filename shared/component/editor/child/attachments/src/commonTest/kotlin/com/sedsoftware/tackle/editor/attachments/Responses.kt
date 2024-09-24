package com.sedsoftware.tackle.editor.attachments

import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType.IMAGE

object Responses {
    val sendFileCorrectResponse: MediaAttachment =
        MediaAttachment(
            id = "123456",
            type = IMAGE,
            url = "http://some.url",
            previewUrl = "http://some.url/preview",
            remoteUrl = "http://some.url/remote",
            description = "Some description",
            blurhash = "abcd",
            meta = null,
        )
}
