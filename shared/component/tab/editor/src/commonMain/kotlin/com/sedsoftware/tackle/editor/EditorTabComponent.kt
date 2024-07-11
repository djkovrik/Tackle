package com.sedsoftware.tackle.editor

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
}
