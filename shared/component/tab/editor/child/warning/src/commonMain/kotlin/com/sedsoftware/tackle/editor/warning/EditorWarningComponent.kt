package com.sedsoftware.tackle.editor.warning

import com.arkivanov.decompose.value.Value

interface EditorWarningComponent {

    val model: Value<Model>

    fun onTextInput(text: String)
    fun clearTextInput()

    data class Model(
        val text: String,
    )
}
