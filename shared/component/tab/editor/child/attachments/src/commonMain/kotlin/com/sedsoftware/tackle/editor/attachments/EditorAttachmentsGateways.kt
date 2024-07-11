package com.sedsoftware.tackle.editor.attachments

import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper

interface EditorAttachmentsGateways {
    interface Api {
        suspend fun sendFile(
            file: PlatformFileWrapper,
            onUpload: (Int) -> Unit = {},
            thumbnail: PlatformFileWrapper? = null,
            description: String? = null,
            focus: String? = null
        ): MediaAttachment
    }
}
