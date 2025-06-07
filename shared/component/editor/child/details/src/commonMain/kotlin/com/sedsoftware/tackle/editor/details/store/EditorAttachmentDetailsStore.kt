package com.sedsoftware.tackle.editor.details.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.details.model.AttachmentParams
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore.Intent
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore.Label
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore.State

internal interface EditorAttachmentDetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OnAlternateTextInput(val text: String) : Intent
        data class OnFocusInput(val x: Float, val y: Float) : Intent
        data object SendAttachmentUpdate : Intent
    }

    data class State(
        val id: String,
        val type: MediaAttachmentType,
        val url: String,
        val params: AttachmentParams,
        val initialDescription: String = "",
        val description: String = "",
        val initialFocus: Pair<Float, Float> = 0f to 0f,
        val focus: Pair<Float, Float> = 0f to 0f,
        val dataChanged: Boolean = false,
        val loading: Boolean = false,
    )

    sealed class Label {
        data object AttachmentDataUpdated : Label()
        data class ErrorCaught(val throwable: Throwable) : Label()
    }
}
