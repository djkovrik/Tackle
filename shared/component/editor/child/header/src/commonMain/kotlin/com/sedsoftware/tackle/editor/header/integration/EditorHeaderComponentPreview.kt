package com.sedsoftware.tackle.editor.header.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent.Model

class EditorHeaderComponentPreview(
    avatar: String = "",
    nickname: String = "",
    domain: String = "",
    recommendedLocale: AppLocale = AppLocale.empty(),
    selectedLocale: AppLocale = AppLocale.empty(),
    availableLocales: List<AppLocale> = emptyList(),
    localePickerAvailable: Boolean = false,
    localePickerDisplayed: Boolean = false,
    statusVisibility: StatusVisibility = StatusVisibility.PUBLIC,
    statusVisibilityPickerDisplayed: Boolean = false,
) : EditorHeaderComponent {

    override val model: Value<Model> =
        MutableValue(
            Model(
                avatar = avatar,
                nickname = nickname,
                domain = domain,
                recommendedLocale = recommendedLocale,
                selectedLocale = selectedLocale,
                availableLocales = availableLocales,
                localePickerAvailable = localePickerAvailable,
                localePickerDisplayed = localePickerDisplayed,
                statusVisibility = statusVisibility,
                statusVisibilityPickerDisplayed = statusVisibilityPickerDisplayed,
                sendButtonAvailable = true,
            )
        )

    override fun onLocalePickerRequested(show: Boolean) = Unit
    override fun onLocaleSelected(language: AppLocale) = Unit
    override fun onStatusVisibilityPickerRequested(show: Boolean) = Unit
    override fun onStatusVisibilitySelected(visibility: StatusVisibility) = Unit
    override fun changeSendingAvailability(available: Boolean) = Unit
}
