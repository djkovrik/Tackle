package com.sedsoftware.tackle.compose.ui.editor.attachment

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent

@Composable
internal fun EditorAttachmentsContent(
    model: EditorAttachmentsComponent.Model,
    modifier: Modifier = Modifier,
    onDelete: (String) -> Unit = {},
    onRetry: (String) -> Unit = {},
) {
}
