package com.sedsoftware.tackle.compose.ui.editor.child.attachment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.editor.child.attachment.content.AttachedFileContent
import com.sedsoftware.tackle.compose.ui.editor.child.attachment.content.AttachmentPreviewImageStub
import com.sedsoftware.tackle.compose.ui.editor.child.attachment.content.getFraction
import com.sedsoftware.tackle.compose.ui.editor.child.attachment.content.getPadding
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@OptIn(ExperimentalLayoutApi::class)
internal fun EditorAttachmentsContent(
    model: EditorAttachmentsComponent.Model,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onDelete: (String) -> Unit = {},
    onRetry: (String) -> Unit = {},
    onEdit: (MediaAttachment) -> Unit = {},
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
                onEdit = { attachment.serverCopy?.let { onEdit.invoke(it) } },
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

@Preview
@Composable
private fun EditorAttachmentsContentPreviewSingleFile() {
    TackleScreenPreview {
        EditorAttachmentsContentPreviewContent(1)
    }
}

@Preview
@Composable
private fun EditorAttachmentsContentPreviewTwoFiles() {
    TackleScreenPreview {
        EditorAttachmentsContentPreviewContent(2)
    }
}

@Preview
@Composable
private fun EditorAttachmentsContentPreviewThreeFiles() {
    TackleScreenPreview {
        EditorAttachmentsContentPreviewContent(3)
    }
}

@Preview
@Composable
private fun EditorAttachmentsContentPreviewFourFiles() {
    TackleScreenPreview {
        EditorAttachmentsContentPreviewContent(4)
    }
}

@Preview
@Composable
private fun EditorAttachmentsContentPreviewFiveFiles() {
    TackleScreenPreview {
        EditorAttachmentsContentPreviewContent(5)
    }
}

@Preview
@Composable
private fun EditorAttachmentsContentPreviewSixFiles() {
    TackleScreenPreview {
        EditorAttachmentsContentPreviewContent(6)
    }
}

@Composable
private fun EditorAttachmentsContentPreviewContent(count: Int) {
    val platformFile = PlatformFileWrapper("", "", "", "", 12345L, "1Mb") { ByteArray(0) }
    val attachedFiles = listOf(
        AttachedFile("id1", platformFile.copy(mimeType = "image/*"),  AttachedFile.Status.LOADING, 0.75f),
        AttachedFile("id2", platformFile.copy(mimeType = "image/*"),  AttachedFile.Status.LOADED, 1f),
        AttachedFile("id3", platformFile.copy(mimeType = "video/*"),  AttachedFile.Status.LOADING, 0.75f),
        AttachedFile("id4", platformFile.copy(mimeType = "audio/*"),  AttachedFile.Status.LOADED, 0.5f),
        AttachedFile("id5", platformFile.copy(mimeType = "image/*"),  AttachedFile.Status.ERROR, 0f),
        AttachedFile("id6", platformFile.copy(mimeType = "unknown/*"),  AttachedFile.Status.LOADED, 1f),
    )

    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.secondaryContainer)) {
        EditorAttachmentsContent(
            model = EditorAttachmentsComponent.Model(
                attachments = attachedFiles.take(count),
                attachmentsButtonAvailable = true,
                attachmentsContentVisible = true,
            ),
            previewImage = { AttachmentPreviewImageStub() },
        )
    }
}
