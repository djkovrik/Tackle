package com.sedsoftware.tackle.editor.details.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.details.model.AttachmentImageParams
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore.Intent
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore.Label
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore.State

internal interface EditorAttachmentDetailsStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class OnAlternateTextInput(val text: String) : Intent()
        data class OnFocusInput(val x: Float, val y: Float) : Intent()
        data object SendAttachmentUpdate : Intent()
    }

    data class State(
        val type: MediaAttachmentType,
        val url: String,
        val id: String,
        val imageParams: AttachmentImageParams,
        val initialDescription: String,
        val description: String,
        val initialFocus: Pair<Float, Float>,
        val focus: Pair<Float, Float>,
        val updatingAvailable: Boolean = false,
        val sending: Boolean = false,
    )

    sealed class Label {
        data object AttachmentDataUpdated : Label()
        data class ErrorCaught(val throwable: Throwable) : Label()
    }
}
