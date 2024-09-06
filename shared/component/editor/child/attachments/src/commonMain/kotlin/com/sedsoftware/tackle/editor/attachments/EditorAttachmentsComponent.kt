package com.sedsoftware.tackle.editor.attachments

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import io.github.vinceglb.filekit.core.PlatformFile

interface EditorAttachmentsComponent {

    val model: Value<Model>

    fun onFilesSelected(files: List<PlatformFile>)
    fun onFilesSelectedWrapped(files: List<PlatformFileWrapper>)
    fun onFileDeleted(id: String)
    fun onFileRetry(id: String)
    fun onAttachmentDetailsClick(attachment: MediaAttachment)
    fun changeComponentAvailability(available: Boolean)
    fun updateInstanceConfig(config: Instance.Config)

    data class Model(
        val attachments: List<AttachedFile>,
        val attachmentsButtonAvailable: Boolean,
        val attachmentsContentVisible: Boolean,
    )
}
