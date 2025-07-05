package com.sedsoftware.tackle.editor.attachments

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import io.github.vinceglb.filekit.PlatformFile

interface EditorAttachmentsComponent {

    val model: Value<Model>

    fun onFilesSelect(files: List<PlatformFile>)
    fun onWrappedFilesSelect(files: List<PlatformFileWrapper>)
    fun onFileDelete(id: String)
    fun onFileRetry(id: String)
    fun onFileEdit(attachment: MediaAttachment)
    fun changeComponentAvailability(available: Boolean)
    fun updateInstanceConfig(config: Instance.Config)

    data class Model(
        val attachments: List<AttachedFile>,
        val attachmentsButtonAvailable: Boolean,
        val attachmentsContentVisible: Boolean,
    )
}
