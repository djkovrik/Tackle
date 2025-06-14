package com.sedsoftware.tackle.editor

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent

interface EditorComponent {
    val attachments: EditorAttachmentsComponent
    val emojis: EditorEmojisComponent
    val header: EditorHeaderComponent
    val poll: EditorPollComponent
    val warning: EditorWarningComponent

    val model: Value<Model>

    val attachmentDetailsDialog: Value<ChildSlot<*, EditorAttachmentDetailsComponent>>

    fun onTextInput(text: String, selection: Pair<Int, Int>)
    fun onEmojiSelect(emoji: CustomEmoji)
    fun onInputHintSelect(hint: EditorInputHintItem)
    fun onPollButtonClick()
    fun onEmojisButtonClick()
    fun onWarningButtonClick()
    fun onScheduleDatePickerRequest(show: Boolean)
    fun onScheduleDateSelect(millis: Long)
    fun onScheduleTimePickerRequest(show: Boolean)
    fun onScheduleTimeSelect(hour: Int, minute: Int, formatIn24hr: Boolean)
    fun resetScheduledDateTime()
    fun onSendButtonClick()
    fun onBackButtonClick()

    data class Model(
        val statusText: String,
        val statusTextSelection: Pair<Int, Int>,
        val statusCharactersLeft: Int,
        val suggestions: List<EditorInputHintItem>,
        val datePickerVisible: Boolean,
        val scheduledDate: Long,
        val timePickerVisible: Boolean,
        val scheduledHour: Int,
        val scheduledMinute: Int,
        val scheduledIn24hrFormat: Boolean,
        val scheduledDateLabel: String,
        val sending: Boolean,
    )
}
