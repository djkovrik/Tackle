package com.sedsoftware.tackle.editor.attachments.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.attachments.model.EditorAttachment
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Intent
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Label
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.State
import io.github.vinceglb.filekit.core.PlatformFile

internal interface EditorAttachmentsStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class OnFilesSelected(val files: List<PlatformFile>) : Intent()
        data class OnRetryClicked(val attachment: EditorAttachment) : Intent()
        data class OnDeleteClicked(val attachment: EditorAttachment) : Intent()
        data class ChangeFeatureState(val available: Boolean) : Intent()
    }

    data class State(
        val config: Instance.Config = Instance.Config(),
        val configLoaded: Boolean = false,
        val selectedFiles: List<EditorAttachment> = emptyList(),
        val attachmentsAvailable: Boolean = false,
    )

    sealed class Label {
        data class ErrorCaught(val throwable: Throwable) : Label()
    }
}
