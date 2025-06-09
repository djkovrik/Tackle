package com.sedsoftware.tackle.editor.header.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.editor.header.store.EditorHeaderStore.Intent
import com.sedsoftware.tackle.editor.header.store.EditorHeaderStore.Label
import com.sedsoftware.tackle.editor.header.store.EditorHeaderStore.State

internal interface EditorHeaderStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OnLocalePickerRequested(val show: Boolean) : Intent
        data class OnLocaleSelected(val language: AppLocale) : Intent
        data class OnVisibilityPickerRequested(val show: Boolean) : Intent
        data class OnVisibilityPickerSelected(val visibility: StatusVisibility) : Intent
        data class ChangeSendingAvailability(val available: Boolean) : Intent
    }

    data class State(
        val avatar: String = "",
        val nickname: String = "",
        val domain: String = "",
        val recommendedLocale: AppLocale = AppLocale.empty(),
        val selectedLocale: AppLocale = AppLocale.empty(),
        val availableLocales: List<AppLocale> = emptyList(),
        val localePickerAvailable: Boolean = false,
        val localePickerDisplayed: Boolean = false,
        val statusVisibility: StatusVisibility = StatusVisibility.PUBLIC,
        val statusVisibilityPickerDisplayed: Boolean = false,
        val sendingAvailable: Boolean = false,
    )

    sealed class Label {
        data class ErrorCaught(val throwable: Throwable) : Label()
    }
}
