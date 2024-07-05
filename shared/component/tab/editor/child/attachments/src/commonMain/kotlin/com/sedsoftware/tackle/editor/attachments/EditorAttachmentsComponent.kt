package com.sedsoftware.tackle.editor.attachments

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.editor.attachments.model.EditorAttachment
import io.github.vinceglb.filekit.core.PlatformFile

interface EditorAttachmentsComponent {

    val model: Value<Model>

    fun onFileSelected(files: List<PlatformFile>)
    fun onRetryClicked(attachment: EditorAttachment)
    fun onDeleteClicked(attachment: EditorAttachment)
    fun changeAttachmentButtonState(available: Boolean)

    data class Model(
        val attachments: List<EditorAttachment>,
        val available: Boolean,
    )
}
