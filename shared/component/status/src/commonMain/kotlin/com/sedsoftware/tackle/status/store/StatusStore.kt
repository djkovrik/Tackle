package com.sedsoftware.tackle.status.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.Translation
import com.sedsoftware.tackle.status.model.StatusContextAction
import com.sedsoftware.tackle.status.store.StatusStore.Intent
import com.sedsoftware.tackle.status.store.StatusStore.Label
import com.sedsoftware.tackle.status.store.StatusStore.State

internal interface StatusStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnDeleteClicked : Intent
        data object OnTranslateClicked : Intent
        data object OnShowOriginalClicked : Intent
        data object OnFavouriteClicked : Intent
        data object OnReblogClicked : Intent
        data object OnBookmarkClicked : Intent
        data object OnPinClicked : Intent
        data object OnMuteClicked : Intent
        data object OnShareClicked : Intent
        data class OnMenuVisibilityChanged(val visible: Boolean) : Intent
        data class OnPollOptionSelected(val index: Int, val multiselect: Boolean) : Intent
        data object OnVoteClicked : Intent
        data class OnUrlClicked(val url: String) : Intent
    }

    data class State(
        val baseStatus: Status,
        val extendedInfo: Boolean,
        val isOwn: Boolean,
        val menuVisible: Boolean = false,
        val menuActions: List<StatusContextAction> = emptyList(),
        val translation: Translation? = null,
        val translationInProgress: Boolean = false,
        val translationDisplayed: Boolean = false,
    )

    sealed class Label {
        data class StatusDeleted(val statusId: String) : Label()
        data class ErrorCaught(val exception: Throwable) : Label()
    }
}
