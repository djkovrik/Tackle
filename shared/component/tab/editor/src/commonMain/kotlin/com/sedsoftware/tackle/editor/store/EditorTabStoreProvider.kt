package com.sedsoftware.tackle.editor.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.domain.EditorTabManager
import com.sedsoftware.tackle.editor.extension.getNewLength
import com.sedsoftware.tackle.editor.extension.getNewPosition
import com.sedsoftware.tackle.editor.extension.insertEmoji
import com.sedsoftware.tackle.editor.extension.insertHint
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.model.EditorInputHintRequest
import com.sedsoftware.tackle.editor.store.EditorTabStore.Intent
import com.sedsoftware.tackle.editor.store.EditorTabStore.Label
import com.sedsoftware.tackle.editor.store.EditorTabStore.State
import com.sedsoftware.tackle.utils.StoreCreate
import com.sedsoftware.tackle.utils.extension.unwrap
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.CoroutineContext

internal class EditorTabStoreProvider(
    private val storeFactory: StoreFactory,
    private val manager: EditorTabManager,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
    private val today: () -> Instant = { System.now() },
) {
    private val todayInstant: Instant by lazy {
        today.invoke()
    }

    @StoreCreate
    fun create(autoInit: Boolean = true): EditorTabStore =
        object : EditorTabStore, Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
            name = "EditorTabStore",
            initialState = State(),
            autoInit = autoInit,
            bootstrapper = coroutineBootstrapper {
                dispatch(Action.FetchCachedInstanceInfo)
                dispatch(Action.InitCurrentTime)
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
                var suggestionJob: Job? = null

                onAction<Action.FetchCachedInstanceInfo> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.getCachedInstanceInfo() },
                            onSuccess = { cachedInstance: Instance ->
                                dispatch(Msg.CachedInstanceLoaded(cachedInstance))
                                dispatch(Msg.StatusCharactersLimitAvailable(cachedInstance.config.statuses.maxCharacters))
                                publish(Label.InstanceConfigLoaded(cachedInstance.config))
                            },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onAction<Action.InitCurrentTime> {
                    val currentDate = todayInstant.toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
                    dispatch(Msg.CurrentTimeLoaded(currentDate.hour, currentDate.minute))
                }

                onAction<Action.CheckForInputHelper> {
                    val currentState = state()
                    val input = currentState.statusText
                    val inputPosition = currentState.statusTextSelection
                    val currentRequest = currentState.currentSuggestionRequest

                    launch {
                        val inputHintRequest = withContext(ioContext) { manager.checkForInputHint(input, inputPosition) }
                        if (inputHintRequest != currentRequest) {
                            dispatch(Msg.InputHintRequestUpdated(inputHintRequest))
                        }

                        when (inputHintRequest) {
                            is EditorInputHintRequest.Accounts -> forward(Action.LoadAccountSuggestion(inputHintRequest.query))
                            is EditorInputHintRequest.Emojis -> forward(Action.LoadEmojiSuggestion(inputHintRequest.query))
                            is EditorInputHintRequest.HashTags -> forward(Action.LoadHashTagSuggestion(inputHintRequest.query))
                            is EditorInputHintRequest.None -> Unit
                        }
                    }
                }

                onAction<Action.LoadAccountSuggestion> {
                    suggestionJob?.cancel()
                    suggestionJob = launch {
                        delay(manager.getInputHintDelay())

                        unwrap(
                            result = withContext(ioContext) { manager.searchForAccounts(it.query) },
                            onSuccess = { accounts: List<EditorInputHintItem> ->
                                dispatch(Msg.SuggestionsLoaded(accounts))
                            },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onAction<Action.LoadEmojiSuggestion> {
                    suggestionJob?.cancel()
                    suggestionJob = launch {
                        delay(manager.getInputHintDelay())

                        unwrap(
                            result = withContext(ioContext) { manager.searchForEmojis(it.query) },
                            onSuccess = { emojis: List<EditorInputHintItem> ->
                                dispatch(Msg.SuggestionsLoaded(emojis))
                            },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onAction<Action.LoadHashTagSuggestion> {
                    suggestionJob?.cancel()
                    suggestionJob = launch {
                        delay(manager.getInputHintDelay())

                        unwrap(
                            result = withContext(ioContext) { manager.searchForHashTags(it.query) },
                            onSuccess = { hashtags: List<EditorInputHintItem> ->
                                dispatch(Msg.SuggestionsLoaded(hashtags))
                            },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onIntent<Intent.OnTextInput> {
                    dispatch(Msg.TextInput(it.text, it.selection))
                    forward(Action.CheckForInputHelper)
                }

                onIntent<Intent.OnEmojiSelect> { dispatch(Msg.EmojiSelected(it.emoji)) }
                onIntent<Intent.OnInputHintSelect> { dispatch(Msg.InputHintSelected(it.hint)) }
                onIntent<Intent.OnRequestDatePicker> { dispatch(Msg.DateDialogVisibilityChanged(it.show)) }
                onIntent<Intent.OnScheduleDate> { dispatch(Msg.ScheduleDateSelected(it.millis)) }
                onIntent<Intent.OnRequestTimePicker> { dispatch(Msg.TimeDialogVisibilityChanged(it.show)) }
                onIntent<Intent.OnScheduleTime> { dispatch(Msg.ScheduleTimeSelected(it.hour, it.minute, it.formatIn24hr)) }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.CachedInstanceLoaded -> copy(
                        instanceInfo = msg.instance,
                        instanceInfoLoaded = true,
                        statusCharactersLimit = msg.instance.config.statuses.maxCharacters,
                    )

                    is Msg.StatusCharactersLimitAvailable -> copy(
                        statusCharactersLeft = msg.limit,
                    )

                    is Msg.CurrentTimeLoaded -> copy(
                        scheduledHour = msg.hour,
                        scheduledMinute = msg.minute,
                        scheduledIn24hFormat = true,
                    )

                    is Msg.TextInput -> copy(
                        statusText = msg.text.take(statusCharactersLimit),
                        statusTextSelection = if (msg.exceedTheLimit(statusCharactersLimit)) {
                            msg.selection.first to statusCharactersLimit
                        } else {
                            msg.selection
                        },
                        statusCharactersLeft = if (msg.underTheLimit(statusCharactersLimit)) {
                            statusCharactersLimit - msg.text.length
                        } else {
                            0
                        },
                    )

                    is Msg.EmojiSelected -> copy(
                        statusText = statusText.insertEmoji(msg.emoji, this),
                        statusTextSelection = statusText.getNewPosition(msg.emoji, this),
                        statusCharactersLeft = statusCharactersLimit - statusText.getNewLength(msg.emoji, this),
                    )

                    is Msg.InputHintRequestUpdated -> copy(
                        currentSuggestionRequest = msg.request,
                        suggestions = if (msg.request !is EditorInputHintRequest.None) {
                            suggestions
                        } else {
                            emptyList()
                        },
                    )

                    is Msg.SuggestionsLoaded -> copy(
                        suggestions = msg.suggestions,
                    )

                    is Msg.InputHintSelected -> copy(
                        statusText = statusText.insertHint(msg.hint, this),
                        statusTextSelection = statusText.getNewPosition(msg.hint, this),
                        statusCharactersLeft = statusCharactersLimit - statusText.getNewLength(msg.hint, this),
                    )

                    is Msg.DateDialogVisibilityChanged -> copy(
                        datePickerVisible = msg.visible,
                    )

                    is Msg.ScheduleDateSelected -> copy(
                        scheduledDate = msg.millis,
                    )

                    is Msg.TimeDialogVisibilityChanged -> copy(
                        timePickerVisible = msg.visible,
                    )

                    is Msg.ScheduleTimeSelected -> copy(
                        scheduledHour = msg.hour,
                        scheduledMinute = msg.minute,
                        scheduledIn24hFormat = msg.formatIn24hr,
                    )
                }
            }
        ) {}

    private sealed interface Action {
        data object FetchCachedInstanceInfo : Action
        data object CheckForInputHelper : Action
        data class LoadAccountSuggestion(val query: String) : Action
        data class LoadEmojiSuggestion(val query: String) : Action
        data class LoadHashTagSuggestion(val query: String) : Action
        data object InitCurrentTime : Action
    }

    private sealed interface Msg {
        data class CachedInstanceLoaded(val instance: Instance) : Msg
        data class StatusCharactersLimitAvailable(val limit: Int) : Msg
        data class CurrentTimeLoaded(val hour: Int, val minute: Int) : Msg
        data class TextInput(val text: String, val selection: Pair<Int, Int>) : Msg
        data class EmojiSelected(val emoji: CustomEmoji) : Msg
        data class InputHintRequestUpdated(val request: EditorInputHintRequest) : Msg
        data class SuggestionsLoaded(val suggestions: List<EditorInputHintItem>) : Msg
        data class InputHintSelected(val hint: EditorInputHintItem) : Msg
        data class DateDialogVisibilityChanged(val visible: Boolean) : Msg
        data class ScheduleDateSelected(val millis: Long) : Msg
        data class TimeDialogVisibilityChanged(val visible: Boolean) : Msg
        data class ScheduleTimeSelected(val hour: Int, val minute: Int, val formatIn24hr: Boolean) : Msg
    }

    private fun Msg.TextInput.exceedTheLimit(limit: Int): Boolean = limit - text.length < 0
    private fun Msg.TextInput.underTheLimit(limit: Int): Boolean = !exceedTheLimit(limit)
}
