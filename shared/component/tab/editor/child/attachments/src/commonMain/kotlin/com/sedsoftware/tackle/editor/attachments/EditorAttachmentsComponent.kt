package com.sedsoftware.tackle.editor.attachments

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import io.github.vinceglb.filekit.core.PlatformFile

interface EditorAttachmentsComponent {

    val model: Value<Model>

    fun onFileSelected(files: List<PlatformFile>)
    fun changeFeatureState(available: Boolean)
    fun updateInstanceConfig(config: Instance.Config)

    data class Model(
        val attachments: List<AttachedFile>,
        val attachmentButtonAvailable: Boolean,
    )
}
