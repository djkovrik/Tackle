package com.sedsoftware.tackle.editor.warning.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent.Model

class EditorWarningComponentPreview(
    warningText: String = "",
    warningContentVisible: Boolean = false,
) : EditorWarningComponent {

    override val model: Value<Model> =
        MutableValue(
            Model(
                text = warningText,
                warningContentVisible = warningContentVisible,
            )
        )

    override fun onTextInput(text: String) = Unit
    override fun onClearTextInput() = Unit
    override fun toggleComponentVisibility() = Unit
    override fun resetComponentState() = Unit
}
