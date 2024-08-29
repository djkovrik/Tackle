package com.sedsoftware.tackle.editor.header

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.type.StatusVisibility

interface EditorHeaderComponent {

    val model: Value<Model>

    fun onLocalePickerRequested(show: Boolean)
    fun onLocaleSelected(language: AppLocale)
    fun onStatusVisibilityPickerRequested(show: Boolean)
    fun onStatusVisibilitySelected(visibility: StatusVisibility)
    fun changeSendingAvailability(available: Boolean)
    fun resetComponentState()

    data class Model(
        val avatar: String,
        val nickname: String,
        val domain: String,
        val recommendedLocale: AppLocale,
        val selectedLocale: AppLocale,
        val availableLocales: List<AppLocale>,
        val localePickerAvailable: Boolean,
        val localePickerDisplayed: Boolean,
        val statusVisibility: StatusVisibility,
        val statusVisibilityPickerDisplayed: Boolean,
        val sendButtonAvailable: Boolean,
    )
}
