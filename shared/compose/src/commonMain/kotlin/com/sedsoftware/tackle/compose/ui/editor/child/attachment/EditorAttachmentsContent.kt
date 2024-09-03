package com.sedsoftware.tackle.compose.ui.editor.child.attachment

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sedsoftware.tackle.compose.ui.editor.child.attachment.content.AttachedFileContent
import com.sedsoftware.tackle.compose.ui.editor.child.attachment.content.getFraction
import com.sedsoftware.tackle.compose.ui.editor.child.attachment.content.getPadding
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile

@Composable
@OptIn(ExperimentalLayoutApi::class)
internal fun EditorAttachmentsContent(
    model: EditorAttachmentsComponent.Model,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onDelete: (String) -> Unit = {},
    onRetry: (String) -> Unit = {},
    previewImage: @Composable (() -> Unit)? = null,
) {
    FlowRow(
        maxItemsInEachRow = 2,
        modifier = modifier,
    ) {
        model.attachments.forEachIndexed { index: Int, attachment: AttachedFile ->
            AttachedFileContent(
                attachment = attachment,
                onDelete = { onDelete.invoke(attachment.id) },
                onRetry = { onRetry.invoke(attachment.id) },
                containerColor = containerColor,
                contentColor = contentColor,
                previewImage = previewImage,
                modifier = Modifier
                    .fillMaxWidth(fraction = getFraction(index, model.attachments.size))
                    .padding(paddingValues = getPadding(index, model.attachments.size)),
            )
        }
    }
}
