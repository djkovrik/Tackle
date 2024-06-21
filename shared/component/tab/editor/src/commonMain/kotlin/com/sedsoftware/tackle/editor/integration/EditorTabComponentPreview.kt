package com.sedsoftware.tackle.editor.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.EditorTabComponent.Model

class EditorTabComponentPreview : EditorTabComponent {

    override val model: Value<Model> = MutableValue(
        Model(text = "Editor tab component")
    )
}
