package com.sedsoftware.tackle.editor.integration

import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.header.integration.EditorHeaderComponentPreview

class EditorTabComponentPreview(
    avatar: String = "",
    nickname: String = "",
    recommendedLocale: AppLocale = AppLocale.empty(),
    selectedLocale: AppLocale = AppLocale.empty(),
    availableLocales: List<AppLocale> = emptyList(),
    localePickerAvailable: Boolean = false,
    localePickerDisplayed: Boolean = false,
    statusVisibility: StatusVisibility = StatusVisibility.PUBLIC,
    statusVisibilityPickerDisplayed: Boolean = false,
) : EditorTabComponent {

    override val header: EditorHeaderComponent =
        EditorHeaderComponentPreview(
            avatar = avatar,
            nickname = nickname,
            recommendedLocale = recommendedLocale,
            selectedLocale = selectedLocale,
            availableLocales = availableLocales,
            localePickerAvailable = localePickerAvailable,
            localePickerDisplayed = localePickerDisplayed,
            statusVisibility = statusVisibility,
            statusVisibilityPickerDisplayed = statusVisibilityPickerDisplayed
        )
}
