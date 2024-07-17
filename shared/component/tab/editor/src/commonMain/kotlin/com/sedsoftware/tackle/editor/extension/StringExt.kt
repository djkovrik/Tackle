package com.sedsoftware.tackle.editor.extension

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.store.EditorTabStore

internal fun String.insertEmoji(emoji: CustomEmoji, state: EditorTabStore.State): String {
    val insertPosition = state.statusTextSelection.second
    val limit = state.statusCharactersLimit
    val split = substring(0, insertPosition) to substring(insertPosition)
    val textWithEmojis = split.first + emoji.shortcode + split.second
    return textWithEmojis.take(limit)
}

internal fun String.getNewPosition(emoji: CustomEmoji, state: EditorTabStore.State): Pair<Int, Int> {
    val limit = state.statusCharactersLimit
    val currentSelection = state.statusTextSelection
    var newSelectionEnd = currentSelection.second + emoji.shortcode.length
    if (newSelectionEnd > limit) newSelectionEnd = limit
    return newSelectionEnd to newSelectionEnd
}

internal fun String.getNewLength(emoji: CustomEmoji, state: EditorTabStore.State): Int {
    val limit = state.statusCharactersLimit
    var newLength = length + emoji.shortcode.length
    if (newLength > limit) newLength = limit
    return newLength
}

