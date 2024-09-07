package com.sedsoftware.tackle.editor.details

import com.sedsoftware.tackle.domain.model.MediaAttachment

interface EditorAttachmentDetailsGateways {
    interface Api {
        suspend fun updateFile(id: String, description: String?, focus: String?): MediaAttachment
        suspend fun getFile(id: String): MediaAttachment
    }
}
