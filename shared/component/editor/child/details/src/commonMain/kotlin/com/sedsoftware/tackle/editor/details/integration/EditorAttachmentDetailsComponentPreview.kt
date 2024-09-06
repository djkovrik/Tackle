package com.sedsoftware.tackle.editor.details.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent.Model

class EditorAttachmentDetailsComponentPreview(
    description: String = "",
    focus: Pair<Float, Float> = 0f to 0f,
    updatingAvailable: Boolean = true,
    sending: Boolean = false,
) : EditorAttachmentDetailsComponent {

    override val model: Value<Model> =
        MutableValue(
            Model(
                description = description,
                focus = focus,
                updatingAvailable = updatingAvailable,
                sending = sending,
            )
        )

    override fun onAttachmentDescriptionInput(text: String) = Unit
    override fun onAttachmentFocusInput(x: Float, y: Float) = Unit
    override fun onUpdateButtonClicked() = Unit
    override fun onBackButtonClicked() = Unit
}
