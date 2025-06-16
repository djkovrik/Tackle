package com.sedsoftware.tackle.domain

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.MediaAttachment

sealed class ComponentOutput {

    sealed class Auth : ComponentOutput() {
        data object AuthFlowCompleted : Auth()
    }

    sealed class HomeTab : ComponentOutput() {
        data object EditorRequested : HomeTab()
        data object ScheduledStatusesRequested : HomeTab()
    }

    sealed class StatusEditor : ComponentOutput() {
        data class EmojiSelected(val emoji: CustomEmoji) : StatusEditor()
        data class PendingAttachmentsCountUpdated(val count: Int) : StatusEditor()
        data class LoadedAttachmentsCountUpdated(val count: Int) : StatusEditor()
        data object StatusPublished : StatusEditor()
        data object ScheduledStatusPublished : StatusEditor()
        data object BackButtonClicked : StatusEditor()
        data class AttachmentDetailsRequested(val attachment: MediaAttachment) : StatusEditor()
        data object AttachmentDataUpdated : StatusEditor()
    }

    sealed class SingleStatus : ComponentOutput() {
        data class Deleted(val statusId: String) : SingleStatus()
        data class ReplyCalled(val statusId: String) : SingleStatus()
        data class HashTagClicked(val hashTag: String) : SingleStatus()
        data class MentionClicked(val mention: String) : SingleStatus()
    }

    sealed class Common : ComponentOutput() {
        data class ErrorCaught(val throwable: Throwable) : Common()
    }
}
