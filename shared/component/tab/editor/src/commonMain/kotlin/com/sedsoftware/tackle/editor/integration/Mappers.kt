package com.sedsoftware.tackle.editor.integration

import com.sedsoftware.tackle.editor.EditorTabComponent.Model
import com.sedsoftware.tackle.editor.store.EditorStore.State

internal val stateToModel: (State) -> Model = {
    Model(
        ownAvatar = it.ownAvatar,
        ownNickname = it.ownNickname,
        textInput = it.textInput,
        symbolsLimitExceeded = it.symbolsLimitExceeded,
        symbolsLeft = it.symbolsLeft,
        emojis = it.emojis.filter { emoji -> emoji.visibleInPicker },
        emojisAvailable = it.emojisAvailable,
        emojiPanelVisible = it.emojiPanelVisible,
        selectedLocale = it.selectedLocale,
        availableLocales = listOf(it.recommendedLocale) + it.availableLocales.filterNot { locale -> locale == it.recommendedLocale },
        localeSelectionAvailable = it.localeSelectionAvailable,
        localePickerVisible = it.localePickerVisible,
    )
}
