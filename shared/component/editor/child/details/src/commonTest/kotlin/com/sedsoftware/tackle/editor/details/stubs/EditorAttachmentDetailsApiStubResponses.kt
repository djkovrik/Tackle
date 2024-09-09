package com.sedsoftware.tackle.editor.details.stubs

import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.MediaAttachmentFocus
import com.sedsoftware.tackle.domain.model.MediaAttachmentMeta
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType

object EditorAttachmentDetailsApiStubResponses {

    val basicResponse: MediaAttachment =
        MediaAttachment(
            id = "123456",
            type = MediaAttachmentType.IMAGE,
            url = "http://some.url",
            previewUrl = "http://some.url/preview",
            remoteUrl = "http://some.url/remote",
            description = "Some description",
            blurhash = "abcd",
            meta = MediaAttachmentMeta(
                length = "",
                duration = 1f,
                fps = 123,
                size = "",
                width = 123,
                height = 123,
                aspect = 1f,
                audioEncode = "",
                audioBitrate = "",
                audioChannels = "",
                focus = MediaAttachmentFocus(1f, 1f),
                original = null,
                small = null,
            ),
        )
}
