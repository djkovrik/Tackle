package com.sedsoftware.tackle.editor.attachments.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Instance.Config
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent.Model
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import io.github.vinceglb.filekit.core.PlatformFile

class EditorAttachmentsComponentPreview(
    attachments: List<AttachedFile> = emptyList(),
    attachmentsButtonAvailable: Boolean = true,
    attachmentsContentVisible: Boolean = false,
) : EditorAttachmentsComponent {

    override val model: Value<Model> =
        MutableValue(
            Model(
                attachments = attachments,
                attachmentsButtonAvailable = attachmentsButtonAvailable,
                attachmentsContentVisible = attachmentsContentVisible,
            )
        )

    override fun onFilesSelected(files: List<PlatformFile>) = Unit
    override fun onFilesSelectedWrapped(files: List<PlatformFileWrapper>) = Unit
    override fun onFileDeleted(id: String) = Unit
    override fun onFileRetry(id: String) = Unit
    override fun onFileEdit(attachment: MediaAttachment) = Unit
    override fun changeComponentAvailability(available: Boolean) = Unit
    override fun updateInstanceConfig(config: Config) = Unit
}
