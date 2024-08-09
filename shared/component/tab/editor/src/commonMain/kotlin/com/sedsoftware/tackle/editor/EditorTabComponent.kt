package com.sedsoftware.tackle.editor

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent

interface EditorTabComponent {
    val attachments: EditorAttachmentsComponent
    val emojis: EditorEmojisComponent
    val header: EditorHeaderComponent
    val poll: EditorPollComponent
    val warning: EditorWarningComponent

    val model: Value<Model>

    fun onTextInput(text: String, selection: Pair<Int, Int>)
    fun onEmojiSelected(emoji: CustomEmoji)
    fun onInputHintSelected(hint: EditorInputHintItem)
    fun onPollButtonClicked()
    fun onEmojisButtonClicked()
    fun onWarningButtonClicked()

    data class Model(
        val statusText: String,
        val statusTextSelection: Pair<Int, Int>,
        val statusCharactersLeft: Int,
        val suggestions: List<EditorInputHintItem>,
    )
}
