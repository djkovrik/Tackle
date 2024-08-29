package com.sedsoftware.tackle.editor.warning.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.editor.warning.store.EditorWarningStore.Intent
import com.sedsoftware.tackle.editor.warning.store.EditorWarningStore.State

internal interface EditorWarningStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class OnTextInput(val text: String) : Intent()
        data object ToggleComponentVisibility : Intent()
    }

    data class State(
        val text: String = "",
        val warningVisible: Boolean = false,
    )
}
