package com.sedsoftware.tackle.domain

import com.sedsoftware.tackle.domain.model.CustomEmoji

sealed class ComponentOutput {

    sealed class Auth : ComponentOutput() {
        data object NavigateToMainScreen : Auth()
    }

    sealed class StatusEditor : ComponentOutput() {
        data class EmojiSelected(val emoji: CustomEmoji) : StatusEditor()
        data class PendingAttachmentsCountUpdated(val count: Int) : StatusEditor()
        data class LoadedAttachmentsCountUpdated(val count: Int) : StatusEditor()
    }

    sealed class Common : ComponentOutput() {
        data class ErrorCaught(val throwable: Throwable) : Common()
    }
}
