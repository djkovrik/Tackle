package com.sedsoftware.tackle.editor.domain

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.NewStatusBundle
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.domain.model.type.CreatedStatusType
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.model.EditorInputHintRequest
import com.sedsoftware.tackle.editor.model.toEditorInputHintAccount
import com.sedsoftware.tackle.editor.model.toEditorInputHintEmoji
import com.sedsoftware.tackle.editor.model.toEditorInputHintHashTag
import kotlinx.coroutines.flow.first

internal class EditorTabManager(
    private val api: EditorTabComponentGateways.Api,
    private val database: EditorTabComponentGateways.Database,
    private val tools: EditorTabComponentGateways.Tools,
) {

    suspend fun getCachedInstanceInfo(): Result<Instance> = runCatching {
        database.getCachedInstanceInfo().first()
    }

    suspend fun searchForAccounts(query: String): Result<List<EditorInputHintItem>> = runCatching {
        val response: Search = api.search(
            query = query,
            type = SEARCH_TYPE_ACCOUNT,
            resolve = false,
            limit = SEARCH_LIMIT,
        )

        response.accounts.map { it.toEditorInputHintAccount() }
    }

    suspend fun searchForEmojis(query: String): Result<List<EditorInputHintItem>> = runCatching {
        val response: List<CustomEmoji> = database.findEmojis(query).first()
        response.take(SEARCH_LIMIT).map { it.toEditorInputHintEmoji() }
    }

    suspend fun searchForHashTags(query: String): Result<List<EditorInputHintItem>> = runCatching {
        val response: Search = api.search(
            query = query,
            type = SEARCH_TYPE_HASHTAG,
            excludeUnreviewed = true,
            limit = SEARCH_LIMIT,
        )

        response.hashtags.map { it.toEditorInputHintHashTag() }
    }

    suspend fun sendStatus(status: NewStatusBundle): Result<CreatedStatusType> = runCatching {
        TODO()
    }

    fun checkForInputHint(inputText: String, inputPosition: Pair<Int, Int>): EditorInputHintRequest {
        val currentPosition = inputPosition.second - 1
        if (currentPosition < 0) return EditorInputHintRequest.None

        val markers = setOf(ACCOUNT_MARKER, EMOJI_MARKER, HASHTAG_MARKER)

        var markerNotUsed = false
        var startIndex = currentPosition
        var endIndex = currentPosition
        var marker: Char = ' '
        var character: Char

        // find lower bound
        for (index in currentPosition downTo 0) {
            character = inputText[index]
            startIndex = index

            if (character.isWhitespace()) {
                markerNotUsed = true
                break
            }

            if (markers.contains(character)) {
                marker = character
                break
            }
        }

        if (markerNotUsed) {
            return EditorInputHintRequest.None
        }

        // find upper bound
        for (index in currentPosition..inputText.lastIndex) {
            character = inputText[index]

            if (character.isWhitespace()) {
                break
            }

            endIndex = index
        }

        // get sequence
        val sequence = inputText.substring(startIndex, endIndex + 1)
        val sequenceLongEnough = sequence.length >= MINIMAL_INPUT_HELP_LENGTH

        return when {
            sequenceLongEnough && marker == ACCOUNT_MARKER -> EditorInputHintRequest.Accounts(sequence)
            sequenceLongEnough && marker == EMOJI_MARKER -> EditorInputHintRequest.Emojis(sequence)
            sequenceLongEnough && marker == HASHTAG_MARKER -> EditorInputHintRequest.HashTags(sequence)
            else -> EditorInputHintRequest.None
        }
    }

    fun getInputHintDelay(): Long = tools.getInputHintDelay()

    private companion object {
        const val SEARCH_TYPE_ACCOUNT = "accounts"
        const val SEARCH_TYPE_HASHTAG = "hashtags"
        const val SEARCH_LIMIT = 10
        const val ACCOUNT_MARKER = '@'
        const val EMOJI_MARKER = ':'
        const val HASHTAG_MARKER = '#'
        const val MINIMAL_INPUT_HELP_LENGTH = 3
    }
}
