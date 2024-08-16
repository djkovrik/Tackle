package com.sedsoftware.tackle.compose.ui.editor.attachment

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.editor.attachment.content.AttachedFileContent
import com.sedsoftware.tackle.compose.ui.editor.attachment.content.AttachmentPreviewImageStub
import com.sedsoftware.tackle.compose.ui.editor.attachment.content.getFraction
import com.sedsoftware.tackle.compose.ui.editor.attachment.content.getPadding
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun EditorAttachmentsContent(
    model: EditorAttachmentsComponent.Model,
    modifier: Modifier = Modifier,
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
                modifier = modifier
                    .fillMaxWidth(fraction = getFraction(index, model.attachments.size))
                    .padding(paddingValues = getPadding(index, model.attachments.size)),
                previewImage = previewImage,
            )
        }
    }
}

@Preview
@Composable
private fun EditorAttachmentsContentOneFilePreview() {
    val platformFile = PlatformFileWrapper("", "", "", "", 0L, "12 Mb") { ByteArray(0) }
    val attachments = listOf(
        AttachedFile("id", platformFile.copy(mimeType = "image"), AttachedFile.Status.LOADED, 0.25f),
    )

    TackleScreenPreview {
        EditorAttachmentsContent(
            model = EditorAttachmentsComponent.Model(
                attachments = attachments,
                attachmentsButtonAvailable = true,
                attachmentsContentVisible = true,
            )
        ) {
            AttachmentPreviewImageStub()
        }
    }
}

@Preview
@Composable
private fun EditorAttachmentsContentTwoFilesPreview() {
    val platformFile = PlatformFileWrapper("", "", "", "", 0L, "12 Mb") { ByteArray(0) }
    val attachments = listOf(
        AttachedFile("id", platformFile.copy(mimeType = "image"), AttachedFile.Status.LOADED, uploadProgress = 0.25f),
        AttachedFile("id", platformFile.copy(mimeType = "video"), AttachedFile.Status.LOADED, 0.5f),
    )

    TackleScreenPreview {
        EditorAttachmentsContent(
            model = EditorAttachmentsComponent.Model(
                attachments = attachments,
                attachmentsButtonAvailable = true,
                attachmentsContentVisible = true,
            )
        ) {
            AttachmentPreviewImageStub()
        }
    }
}

@Preview
@Composable
private fun EditorAttachmentsContentThreeFilesPreview() {
    val platformFile = PlatformFileWrapper("", "", "", "", 0L, "12 Mb") { ByteArray(0) }
    val attachments = listOf(
        AttachedFile("id", platformFile.copy(mimeType = "image"), AttachedFile.Status.LOADED, 0.25f),
        AttachedFile("id", platformFile.copy(mimeType = "video"), AttachedFile.Status.LOADED, 0.5f),
        AttachedFile("id", platformFile.copy(mimeType = "audio"), AttachedFile.Status.LOADED, 0.75f),
    )

    TackleScreenPreview {
        EditorAttachmentsContent(
            model = EditorAttachmentsComponent.Model(
                attachments = attachments,
                attachmentsButtonAvailable = true,
                attachmentsContentVisible = true,
            )
        )
    }
}

@Preview
@Composable
private fun EditorAttachmentsContentFourFilesPreview() {
    val platformFile = PlatformFileWrapper("", "", "", "", 0L, "12 Mb") { ByteArray(0) }
    val attachments = listOf(
        AttachedFile("id", platformFile.copy(mimeType = "image"), AttachedFile.Status.LOADED, 0.25f),
        AttachedFile("id", platformFile.copy(mimeType = "video"), AttachedFile.Status.LOADED, 0.5f),
        AttachedFile("id", platformFile.copy(mimeType = "audio"), AttachedFile.Status.LOADED, 0.75f),
        AttachedFile("id", platformFile.copy(mimeType = "abcs"), AttachedFile.Status.LOADED, 1f),
    )

    TackleScreenPreview {
        EditorAttachmentsContent(
            model = EditorAttachmentsComponent.Model(
                attachments = attachments,
                attachmentsButtonAvailable = true,
                attachmentsContentVisible = true,
            )
        )
    }
}
