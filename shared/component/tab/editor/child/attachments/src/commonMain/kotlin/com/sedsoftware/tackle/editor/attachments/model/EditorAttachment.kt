package com.sedsoftware.tackle.editor.attachments.model

import com.sedsoftware.tackle.domain.model.MediaAttachment
import io.github.vinceglb.filekit.core.PlatformFile

data class EditorAttachment(
    val file: PlatformFile,
    val status: Status,
    val serverCopy: MediaAttachment? = null,
) {
    enum class Status {
        PENDING, LOADING, LOADED, ERROR;
    }
}
