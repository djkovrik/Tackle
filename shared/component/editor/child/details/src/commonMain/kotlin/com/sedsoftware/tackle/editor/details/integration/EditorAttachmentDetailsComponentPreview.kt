package com.sedsoftware.tackle.editor.details.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent.Model
import com.sedsoftware.tackle.editor.details.model.AttachmentImageParams

class EditorAttachmentDetailsComponentPreview(
    description: String = "",
    focus: Pair<Float, Float> = 0f to 0f,
    updatingAvailable: Boolean = true,
    sending: Boolean = false,
) : EditorAttachmentDetailsComponent {

    override val model: Value<Model> =
        MutableValue(
            Model(
                type = MediaAttachmentType.IMAGE,
                url = "",
                description = description,
                imageParams = AttachmentImageParams(
                    blurhash = "UJOoqX\$P*|oz}@%gELX9+sIW9vrr?GZhxYVs",
                    width = 588,
                    height = 392,
                    ratio = 588f / 392f,
                ),
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
