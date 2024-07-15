package com.sedsoftware.tackle.editor

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent

interface EditorTabComponent {
    val attachments: EditorAttachmentsComponent
    val emojis: EditorEmojisComponent
    val header: EditorHeaderComponent
    val poll: EditorPollComponent
    val warning: EditorWarningComponent

    val model: Value<Model>

    fun onTextInput(text: String)
    fun onEmojiSelected(emoji: CustomEmoji)
    fun onAttachmentsButtonClicked()
    fun onPollButtonClicked()
    fun onEmojisButtonClicked()
    fun onWarningButtonClicked()
    fun onSendButtonClicked()

    data class Model(
        val statusText: String,
        val statusCharactersLeft: Int,
        val attachmentsActive: Boolean,
        val emojisActive: Boolean,
        val pollActive: Boolean,
        val warningActive: Boolean,
        val sendingAvailable: Boolean,
    )
}
