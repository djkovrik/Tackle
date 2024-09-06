package com.sedsoftware.tackle.editor.details

import com.arkivanov.decompose.value.Value

interface EditorAttachmentDetailsComponent {

    val model: Value<Model>

    fun onAttachmentDescriptionInput(text: String)
    fun onAttachmentFocusInput(x: Float, y: Float)
    fun onUpdateButtonClicked()
    fun onBackButtonClicked()

    data class Model(
        val description: String,
        val focus: Pair<Float, Float>,
        val updatingAvailable: Boolean,
        val sending: Boolean,
    )
}
