package com.sedsoftware.tackle.editor.attachments.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.Instance.Config
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent.Model
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import io.github.vinceglb.filekit.core.PlatformFile

class EditorAttachmentsComponentPreview(
    attachments: List<AttachedFile> = emptyList(),
    available: Boolean = false,
) : EditorAttachmentsComponent {

    override val model: Value<Model> =
        MutableValue(
            Model(
                attachments = attachments,
                available = available,
            )
        )

    override fun onFileSelected(files: List<PlatformFile>) = Unit
    override fun changeFeatureState(available: Boolean) = Unit
    override fun updateInstanceConfig(config: Config) = Unit
}
