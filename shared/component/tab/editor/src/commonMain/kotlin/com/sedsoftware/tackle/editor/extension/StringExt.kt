package com.sedsoftware.tackle.editor.extension

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.model.EditorInputHintRequest
import com.sedsoftware.tackle.editor.store.EditorTabStore

internal fun String.insertEmoji(emoji: CustomEmoji, state: EditorTabStore.State): String {
    val code = ":${emoji.shortcode}:"
    val insertPosition = state.statusTextSelection.second
    val limit = state.statusCharactersLimit
    val split = substring(0, insertPosition) to substring(insertPosition)
    val textWithEmojis = split.first + code + split.second
    return textWithEmojis.take(limit)
}

internal fun String.getNewPosition(emoji: CustomEmoji, state: EditorTabStore.State): Pair<Int, Int> {
    val code = ":${emoji.shortcode}:"
    val limit = state.statusCharactersLimit
    val currentSelection = state.statusTextSelection
    var newSelectionEnd = currentSelection.second + code.length
    if (newSelectionEnd > limit) newSelectionEnd = limit
    return newSelectionEnd to newSelectionEnd
}

internal fun String.getNewLength(emoji: CustomEmoji, state: EditorTabStore.State): Int {
    val code = ":${emoji.shortcode}:"
    val limit = state.statusCharactersLimit
    var newLength = length + code.length
    if (newLength > limit) newLength = limit
    return newLength
}

internal fun String.insertHint(hint: EditorInputHintItem, state: EditorTabStore.State): String {
    val markers = setOf(':', '#', '@')
    val currentText = state.statusText
    val currentSelection = state.statusTextSelection
    val limit = state.statusCharactersLimit

    val textToInsert = when (hint) {
        is EditorInputHintItem.Account -> "@${hint.username}"
        is EditorInputHintItem.Emoji -> ":${hint.shortcode}:"
        is EditorInputHintItem.HashTag -> "#${hint.text}"
    }

    val currentPosition = currentSelection.second - 1
    var startIndex = currentPosition
    var endIndex = currentPosition
    var marker: Char = ' '
    var character: Char

    // find lower bound
    for (index in currentPosition downTo 0) {
        character = currentText[index]
        startIndex = index

        if (markers.contains(character)) {
            marker = character
            break
        }
    }

    // find upper bound
    for (index in currentPosition..currentText.lastIndex) {
        character = currentText[index]

        if (character.isWhitespace()) {
            break
        }

        endIndex = index
    }

    val chunkedText = listOf(
        currentText.substring(0, startIndex),
        currentText.substring(startIndex, endIndex + 1),
        currentText.substring(endIndex + 1),
    )

    val result = chunkedText[0] + textToInsert + chunkedText[2]
    return result.take(limit)
}

internal fun String.getNewPosition(hint: EditorInputHintItem, state: EditorTabStore.State): Pair<Int, Int> {
    val markers = setOf(':', '#', '@')
    val currentText = state.statusText
    val currentSelection = state.statusTextSelection
    val textToInsert = when (hint) {
        is EditorInputHintItem.Account -> "@${hint.username}"
        is EditorInputHintItem.Emoji -> ":${hint.shortcode}:"
        is EditorInputHintItem.HashTag -> "#${hint.text}"
    }

    val currentPosition = currentSelection.second - 1
    var startIndex = currentPosition
    var character: Char

    // find lower bound
    for (index in currentPosition downTo 0) {
        character = currentText[index]
        startIndex = index

        if (markers.contains(character)) {
            break
        }
    }

    var resultingPosition = startIndex + textToInsert.length
    if (resultingPosition > state.statusCharactersLimit) resultingPosition = state.statusCharactersLimit

    return resultingPosition to resultingPosition
}

internal fun String.getNewLength(hint: EditorInputHintItem, state: EditorTabStore.State): Int {
    val currentRequest = when (val request = state.currentSuggestionRequest) {
        is EditorInputHintRequest.Accounts -> request.query
        is EditorInputHintRequest.Emojis -> request.query
        is EditorInputHintRequest.HashTags -> request.query
        else -> ""
    }
    val textToInsert = when (hint) {
        is EditorInputHintItem.Account -> "@${hint.username}"
        is EditorInputHintItem.Emoji -> ":${hint.shortcode}:"
        is EditorInputHintItem.HashTag -> "#${hint.text}"
    }
    val limit = state.statusCharactersLimit
    var newLength = length + textToInsert.length - currentRequest.length
    if (newLength > limit) newLength = limit
    return newLength
}
