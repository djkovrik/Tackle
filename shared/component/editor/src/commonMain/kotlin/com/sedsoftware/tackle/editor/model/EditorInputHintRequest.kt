package com.sedsoftware.tackle.editor.model

sealed class EditorInputHintRequest {
    data class Accounts(val query: String) : EditorInputHintRequest()
    data class Emojis(val query: String) : EditorInputHintRequest()
    data class HashTags(val query: String) : EditorInputHintRequest()
    data object None : EditorInputHintRequest()
}
