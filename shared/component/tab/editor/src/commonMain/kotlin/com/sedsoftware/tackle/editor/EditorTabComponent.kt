package com.sedsoftware.tackle.editor

import com.arkivanov.decompose.value.Value

interface EditorTabComponent {
    val model: Value<Model>

    data class Model(
        val text: String,
    )

    sealed class Output {
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
