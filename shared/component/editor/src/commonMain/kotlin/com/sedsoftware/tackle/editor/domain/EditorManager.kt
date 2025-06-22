package com.sedsoftware.tackle.editor.domain

import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.NewStatusBundle
import com.sedsoftware.tackle.domain.model.ScheduledStatus
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.domain.model.SearchRequestBundle
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.editor.EditorComponentGateways
import com.sedsoftware.tackle.editor.extension.hasScheduledDate
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.model.EditorInputHintRequest
import com.sedsoftware.tackle.editor.model.toEditorInputHintAccount
import com.sedsoftware.tackle.editor.model.toEditorInputHintEmoji
import com.sedsoftware.tackle.editor.model.toEditorInputHintHashTag
import com.sedsoftware.tackle.utils.DateTimeUtils
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

internal class EditorManager(
    private val api: EditorComponentGateways.Api,
    private val database: EditorComponentGateways.Database,
    private val tools: EditorComponentGateways.Tools,
    private val nowInstantProvider: () -> Instant = { System.now() },
    private val timeZoneProvider: () -> TimeZone = { TimeZone.currentSystemDefault() },
) {

    suspend fun getCachedInstanceInfo(): Result<Instance> = runCatching {
        database.getCachedInstanceInfo().first()
    }

    suspend fun searchForAccounts(query: String): Result<List<EditorInputHintItem>> = runCatching {
        val response: Search = api.search(
            SearchRequestBundle(
                query = query,
                type = SEARCH_TYPE_ACCOUNT,
                resolve = false,
                limit = SEARCH_LIMIT,
            )
        )

        response.accounts.map { it.toEditorInputHintAccount() }
    }

    suspend fun searchForEmojis(query: String): Result<List<EditorInputHintItem>> = runCatching {
        val response: List<CustomEmoji> = database.findEmojis(query).first()
        response.take(SEARCH_LIMIT).map { it.toEditorInputHintEmoji() }
    }

    suspend fun searchForHashTags(query: String): Result<List<EditorInputHintItem>> = runCatching {
        val response: Search = api.search(
            SearchRequestBundle(
                query = query,
                type = SEARCH_TYPE_HASHTAG,
                excludeUnreviewed = true,
                limit = SEARCH_LIMIT,
            )
        )

        response.hashtags.map { it.toEditorInputHintHashTag() }
    }

    suspend fun sendStatus(bundle: NewStatusBundle): Result<Status> = runCatching {
        val response = api.sendStatus(bundle)
        return@runCatching response
    }

    suspend fun sendScheduledStatus(bundle: NewStatusBundle): Result<ScheduledStatus> = runCatching {
        val timeZone: TimeZone = timeZoneProvider.invoke()
        val now: Instant = nowInstantProvider.invoke()
        val scheduled: Instant = if (bundle.hasScheduledDate) {
            DateTimeUtils
                .getDateTimeFromPickers(bundle.scheduledAtDate, bundle.scheduledAtHour, bundle.scheduledAtMinute, timeZone)
                .toInstant(timeZone)
        } else {
            now
        }

        val duration: Duration = scheduled - now
        val hasValidScheduleDate: Boolean = duration > SCHEDULED_STATUS_MIN_PERIOD.minutes

        println("Now $now, schedule $scheduled, duration $duration")

        if (bundle.hasScheduledDate && !hasValidScheduleDate) {
            throw TackleException.ScheduleDateTooShort
        }

        val response = api.sendStatusScheduled(bundle)
        return@runCatching response
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
        const val SCHEDULED_STATUS_MIN_PERIOD = 10
    }
}
