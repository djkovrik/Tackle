package com.sedsoftware.tackle.editor.header

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.type.StatusVisibility

interface EditorHeaderComponent {

    val model: Value<Model>

    fun onLocalePickerRequest(show: Boolean)
    fun onLocaleSelect(language: AppLocale)
    fun onStatusVisibilityPickerRequest(show: Boolean)
    fun onStatusVisibilitySelect(visibility: StatusVisibility)
    fun changeSendingAvailability(available: Boolean)

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
