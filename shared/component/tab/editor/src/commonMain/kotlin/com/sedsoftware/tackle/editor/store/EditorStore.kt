package com.sedsoftware.tackle.editor.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.editor.store.EditorStore.Intent
import com.sedsoftware.tackle.editor.store.EditorStore.Label
import com.sedsoftware.tackle.editor.store.EditorStore.State
import com.sedsoftware.tackle.network.model.CustomEmoji
import com.sedsoftware.tackle.utils.model.AppLocale

internal interface EditorStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class OnTextInput(val text: String) : Intent()
        data class OnShowEmojiPanel(val show: Boolean) : Intent()
        data class OnShowLanguagePicker(val show: Boolean) : Intent()
        data class OnLocaleSelected(val language: AppLocale) : Intent()
    }

    data class State(
        val ownAvatar: String = "",
        val ownNickname: String = "",
        val textInput: String = "",
        val symbolsLimitExceeded: Boolean = true,
        val symbolsLeft: Int = 0,
        val emojis: List<CustomEmoji> = emptyList(),
        val emojisAvailable: Boolean = false,
        val emojiPanelVisible: Boolean = false,
        val selectedLocale: AppLocale = AppLocale.empty(),
        val availableLocales: List<AppLocale> = emptyList(),
        val localeSelectionAvailable: Boolean = false,
        val localePickerVisible: Boolean = false,
    )

    sealed class Label {
        data class ErrorCaught(val throwable: Throwable) : Label()
    }
}
