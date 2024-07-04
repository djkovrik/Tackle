package com.sedsoftware.tackle.editor

import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent

interface EditorTabComponent {
    val header: EditorHeaderComponent
    val emojis: EditorEmojisComponent
}
