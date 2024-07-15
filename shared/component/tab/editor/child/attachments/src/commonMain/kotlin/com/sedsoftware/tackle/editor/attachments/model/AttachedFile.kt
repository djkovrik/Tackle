package com.sedsoftware.tackle.editor.attachments.model

import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper

data class AttachedFile(
    val id: String,
    val file: PlatformFileWrapper,
    val status: Status,
    val uploadProgress: Int = 0,
    val serverCopy: MediaAttachment? = null,
) {
    enum class Status {
        PENDING, LOADING, LOADED, ERROR;
    }
}
