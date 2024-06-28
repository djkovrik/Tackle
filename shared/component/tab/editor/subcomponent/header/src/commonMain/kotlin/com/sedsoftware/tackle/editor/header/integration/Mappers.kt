package com.sedsoftware.tackle.editor.header.integration

import com.sedsoftware.tackle.editor.header.EditorHeaderComponent.Model
import com.sedsoftware.tackle.editor.header.store.EditorHeaderStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        avatar = it.avatar,
        nickname = it.nickname,
        recommendedLocale = it.recommendedLocale,
        selectedLocale = it.selectedLocale,
        availableLocales = listOf(it.recommendedLocale) + it.availableLocales.filterNot { locale -> locale == it.recommendedLocale },
        localePickerAvailable = it.localePickerAvailable,
        localePickerDisplayed = it.localePickerDisplayed,
        statusVisibility = it.statusVisibility,
        statusVisibilityPickerDisplayed = it.statusVisibilityPickerDisplayed,
    )
}
