package com.sedsoftware.tackle.compose.ui.editor.content

import com.sedsoftware.tackle.compose.model.EditorToolbarItem
import com.sedsoftware.tackle.editor.EditorComponent
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent

internal fun buildToolbarState(
    editor: EditorComponent.Model,
    attachments: EditorAttachmentsComponent.Model,
    emojis: EditorEmojisComponent.Model,
    poll: EditorPollComponent.Model,
    warning: EditorWarningComponent.Model,
): List<EditorToolbarItem> = listOf(
    EditorToolbarItem(
        type = EditorToolbarItem.Type.ATTACH,
        active = attachments.attachmentsContentVisible,
        enabled = attachments.attachmentsButtonAvailable,
    ),
    EditorToolbarItem(
        type = EditorToolbarItem.Type.EMOJIS,
        active = emojis.emojisContentVisible,
        enabled = emojis.emojisButtonAvailable,
    ),
    EditorToolbarItem(
        type = EditorToolbarItem.Type.POLL,
        active = poll.pollContentVisible,
        enabled = poll.pollButtonAvailable,
    ),
    EditorToolbarItem(
        type = EditorToolbarItem.Type.WARNING,
        active = warning.warningContentVisible,
        enabled = true,
    ),
    EditorToolbarItem(
        type = EditorToolbarItem.Type.SCHEDULE,
        active = editor.datePickerVisible || editor.timePickerVisible || editor.scheduledDateLabel.isNotEmpty(),
        enabled = true,
    ),
)
