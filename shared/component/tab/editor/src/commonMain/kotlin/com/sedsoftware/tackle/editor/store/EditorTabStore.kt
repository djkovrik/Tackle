package com.sedsoftware.tackle.editor.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.model.EditorInputHintRequest
import com.sedsoftware.tackle.editor.store.EditorTabStore.Intent
import com.sedsoftware.tackle.editor.store.EditorTabStore.Label
import com.sedsoftware.tackle.editor.store.EditorTabStore.State
import kotlinx.datetime.LocalDateTime

interface EditorTabStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class OnTextInput(val text: String, val selection: Pair<Int, Int>) : Intent()
        data class OnEmojiSelect(val emoji: CustomEmoji) : Intent()
        data class OnInputHintSelect(val hint: EditorInputHintItem) : Intent()
        data class OnRequestDatePicker(val show: Boolean) : Intent()
        data class OnScheduleDate(val millis: Long) : Intent()
    }

    data class State(
        val statusText: String = "",
        val statusTextSelection: Pair<Int, Int> = (0 to 0),
        val statusCharactersLeft: Int = -1,
        val statusCharactersLimit: Int = -1,
        val instanceInfo: Instance = Instance.empty(),
        val instanceInfoLoaded: Boolean = false,
        val suggestions: List<EditorInputHintItem> = emptyList(),
        val currentSuggestionRequest: EditorInputHintRequest = EditorInputHintRequest.None,
        val datePickerVisible: Boolean = false,
        val scheduledAt: Long = 0L,
    )

    sealed class Label {
        data class InstanceConfigLoaded(val config: Instance.Config) : Label()
        data class ErrorCaught(val throwable: Throwable) : Label()
    }
}
