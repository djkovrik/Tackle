package com.sedsoftware.tackle.editor.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.EditorTabComponent.Model

class EditorTabComponentPreview(
    ownAvatar: String = "",
    ownNickname: String = "",
    textInput: String = "",
    symbolsLimitExceeded: Boolean = true,
    symbolsLeft: Int = 0,
    emojis: List<CustomEmoji> = emptyList(),
    emojisAvailable: Boolean = false,
    emojiPanelVisible: Boolean = false,
    recommendedLocale: AppLocale = AppLocale.empty(),
    selectedLocale: AppLocale = AppLocale.empty(),
    availableLocales: List<AppLocale> = emptyList(),
    localeSelectionAvailable: Boolean = false,
    localePickerVisible: Boolean = false,
) : EditorTabComponent {

    override val model: Value<Model> =
        MutableValue(
            Model(
                ownAvatar = ownAvatar,
                ownNickname = ownNickname,
                textInput = textInput,
                symbolsLimitExceeded = symbolsLimitExceeded,
                symbolsLeft = symbolsLeft,
                emojis = emojis,
                emojisAvailable = emojisAvailable,
                emojiPanelVisible = emojiPanelVisible,
                recommendedLocale = recommendedLocale,
                selectedLocale = selectedLocale,
                availableLocales = availableLocales,
                localeSelectionAvailable = localeSelectionAvailable,
                localePickerVisible = localePickerVisible,
            )
        )

    override fun onTextInput(text: String) = Unit
    override fun onEmojiPanelRequest(requested: Boolean) = Unit
    override fun onLanguagePickerRequest(show: Boolean) = Unit
    override fun onLocaleSelect(language: AppLocale) = Unit
}
