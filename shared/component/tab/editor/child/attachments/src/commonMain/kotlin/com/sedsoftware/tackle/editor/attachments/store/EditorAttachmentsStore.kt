package com.sedsoftware.tackle.editor.attachments.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Intent
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Label
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.State

internal interface EditorAttachmentsStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class OnFilesSelected(val files: List<PlatformFileWrapper>) : Intent()
        data class OnFileDeleted(val id: String) : Intent()
        data class ChangeComponentAvailability(val available: Boolean) : Intent()
        data class UpdateInstanceConfig(val config: Instance.Config) : Intent()
    }

    data class State(
        val config: Instance.Config = Instance.Config(),
        val configLoaded: Boolean = false,
        val selectedFiles: List<AttachedFile> = emptyList(),
        val attachmentsAtLimit: Boolean = false,
        val attachmentsAvailable: Boolean = true,
        val attachmentsVisible: Boolean = false,
        val hasUploadInProgress: Boolean = false,
    )

    sealed class Label {
        data class AttachmentsCountUpdated(val count: Int) : Label()
        data class ErrorCaught(val throwable: Throwable) : Label()
    }
}
