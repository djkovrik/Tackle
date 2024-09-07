package com.sedsoftware.tackle.editor.details

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.details.model.AttachmentImageParams

interface EditorAttachmentDetailsComponent {

    val model: Value<Model>

    fun onAttachmentDescriptionInput(text: String)
    fun onAttachmentFocusInput(x: Float, y: Float)
    fun onUpdateButtonClicked()
    fun onBackButtonClicked()

    data class Model(
        val type: MediaAttachmentType,
        val url: String,
        val description: String,
        val imageParams: AttachmentImageParams,
        val focus: Pair<Float, Float>,
        val updatingAvailable: Boolean,
        val sending: Boolean,
    )
}
