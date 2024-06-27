package com.sedsoftware.tackle.editor

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.AppLocale

interface EditorTabComponent {
    val model: Value<Model>

    fun onTextInput(text: String)
    fun onEmojiPanelRequest(requested: Boolean)
    fun onLanguagePickerRequest(show: Boolean)
    fun onLocaleSelect(language: AppLocale)

    data class Model(
        val ownAvatar: String,
        val ownNickname: String,
        val textInput: String,
        val symbolsLimitExceeded: Boolean,
        val symbolsLeft: Int,
        val emojis: List<CustomEmoji>,
        val emojisAvailable: Boolean,
        val emojiPanelVisible: Boolean,
        val recommendedLocale: AppLocale,
        val selectedLocale: AppLocale,
        val availableLocales: List<AppLocale>,
        val localeSelectionAvailable: Boolean,
        val localePickerVisible: Boolean,
    )

    sealed class Output {
        data class ErrorCaught(val throwable: Throwable) : Output()
    }
}
