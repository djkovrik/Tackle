package com.sedsoftware.tackle.editor.content.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.content.store.EditorEmojisStore.Label
import com.sedsoftware.tackle.editor.content.store.EditorEmojisStore.State

internal interface EditorEmojisStore : Store<Nothing, State, Label> {

    data class State(
        val emojiPickerAvailable: Boolean = false,
        val emojis: List<CustomEmoji> = emptyList(),
    )

    sealed class Label {
        data class ErrorCaught(val throwable: Throwable) : Label()
    }
}
