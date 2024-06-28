package com.sedsoftware.tackle.editor

import com.sedsoftware.tackle.editor.header.EditorHeaderComponent

interface EditorTabComponent {
    val header: EditorHeaderComponent

    sealed class Output {
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
