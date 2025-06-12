package com.sedsoftware.tackle.editor.emojis.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.emojis.store.EditorEmojisStore.Intent
import com.sedsoftware.tackle.editor.emojis.store.EditorEmojisStore.Label
import com.sedsoftware.tackle.editor.emojis.store.EditorEmojisStore.State

internal interface EditorEmojisStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ToggleComponentVisibility : Intent
    }

    data class State(
        val emojis: Map<String, List<CustomEmoji>> = emptyMap(),
        val emojisAvailable: Boolean = false,
        val emojisVisible: Boolean = false,
    )

    sealed class Label {
        data class ErrorCaught(val throwable: Throwable) : Label()
    }
}
