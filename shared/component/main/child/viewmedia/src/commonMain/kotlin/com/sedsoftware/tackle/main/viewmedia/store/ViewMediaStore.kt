package com.sedsoftware.tackle.main.viewmedia.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.main.viewmedia.store.ViewMediaStore.Intent
import com.sedsoftware.tackle.main.viewmedia.store.ViewMediaStore.Label
import com.sedsoftware.tackle.main.viewmedia.store.ViewMediaStore.State
import io.github.vinceglb.filekit.PlatformFile

interface ViewMediaStore : Store<Intent, State, Label> {
    sealed interface Intent {
        data class OnSelectionChanged(val index: Int) : Intent
        data class OnDownloadClicked(val destination: PlatformFile) : Intent
    }

    data class State(
        val attachments: List<MediaAttachment>,
        val selectedIndex: Int,
        val downloadInProgress: List<Boolean> = List(attachments.size) { false },
        val downloadProgress: List<Float> = List(attachments.size) { 0f },
        val downloadCompleted: List<Boolean> = List(attachments.size) { false },
    )

    sealed class Label {
        data class ErrorCaught(val exception: Throwable) : Label()
    }
}
