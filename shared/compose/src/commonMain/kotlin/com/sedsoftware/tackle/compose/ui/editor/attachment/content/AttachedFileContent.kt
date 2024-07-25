package com.sedsoftware.tackle.compose.ui.editor.attachment.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.getTypeTitle
import com.sedsoftware.tackle.compose.extension.hasError
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.utils.extension.isImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_attachment_failed
import tackle.shared.compose.generated.resources.editor_delete
import tackle.shared.compose.generated.resources.preview_sample

@Composable
internal fun AttachedFileContent(
    attachment: AttachedFile,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    onDelete: () -> Unit = {},
    previewImage: @Composable (() -> Unit)? = null,
) {
    var imagePreviewData: ByteArray = remember { ByteArray(0) }

    LaunchedEffect(attachment.id) {
        if (attachment.file.isImage) {
            imagePreviewData = attachment.file.readBytes()
        }
    }

    val containerShape = RoundedCornerShape(size = 8.dp)

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.25f,
                ),
                shape = containerShape
            )
            .clip(shape = containerShape),
    ) {
        // Image
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 200.dp)
        ) {
            when {
                // Preview stub
                attachment.file.isImage && previewImage != null -> {
                    previewImage.invoke()
                }
                // Preview from local copy
                imagePreviewData.isNotEmpty() -> {
                    AttachedFileImageThumbnail(
                        imageData = imagePreviewData,
                        modifier = Modifier,
                    )
                }
                // Generic stub
                else -> {
                    AttachedFileContentGeneric(
                        attachment = attachment,
                        modifier = Modifier,
                    )
                }
            }

            // Upload failed indicator
            if (attachment.hasError) {
                AttachedFileUploadFailed(
                    onClick = onRetry,
                    indicatorSize = 48.dp,
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .align(alignment = Alignment.TopEnd)
                )
            }
        }

        Column(
            modifier = Modifier
                .background(
                    color = if (attachment.hasError) {
                        MaterialTheme.colorScheme.errorContainer
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    }
                )
                .fillMaxWidth()
                .padding(all = 4.dp)
        ) {
            // Progress
            LinearProgressIndicator(
                progress = { attachment.uploadProgress / 100f },
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            // Info bar
            // (size + media type) --- buttons: edit - retry - delete
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = stringResource(resource = attachment.getTypeTitle()),
                        color = if (attachment.hasError) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )

                    Text(
                        text = if (attachment.hasError) {
                            stringResource(resource = Res.string.editor_attachment_failed)
                        } else {
                            attachment.file.sizeLabel
                        },
                        color = if (attachment.hasError) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }

                Spacer(modifier = Modifier.weight(weight = 1f, fill = true))

                IconButton(onClick = onDelete) {
                    Icon(
                        painterResource(resource = Res.drawable.editor_delete),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(size = 24.dp),
                    )
                }
            }
        }
    }
}

@Composable
internal fun AttachmentPreviewImageStub() {
    Image(
        painter = painterResource(resource = Res.drawable.preview_sample),
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}

@Preview
@Composable
private fun AttachedFileContentPreview() {
    val platformFile = PlatformFileWrapper(
        name = "test.jpg",
        extension = "jpg",
        path = "",
        mimeType = "image/jpeg",
        size = 0L,
        sizeLabel = "123 Mb",
        readBytes = { ByteArray(0) },
    )

    val image = AttachedFile(id = "id", file = platformFile, status = AttachedFile.Status.LOADING, uploadProgress = 75)

    TackleScreenPreview {
        Column {
            // Image
            Box(modifier = Modifier.width(width = 420.dp).padding(all = 8.dp)) {
                AttachedFileContent(attachment = image) {
                    AttachmentPreviewImageStub()
                }
            }
            // Error
            Box(modifier = Modifier.width(width = 420.dp).padding(all = 8.dp)) {
                AttachedFileContent(attachment = image.copy(status = AttachedFile.Status.ERROR)) {
                    AttachmentPreviewImageStub()
                }
            }
            // Error
            Box(modifier = Modifier.width(width = 420.dp).padding(all = 8.dp)) {
                AttachedFileContent(attachment = image.copy(status = AttachedFile.Status.ERROR))
            }
        }
    }
}

@Preview
@Composable
private fun AttachedFileContentPlaceholdersPreview() {
    val platformFile = PlatformFileWrapper(
        name = "test.jpg",
        extension = "jpg",
        path = "",
        mimeType = "image/jpeg",
        size = 0L,
        sizeLabel = "123 Mb",
        readBytes = { ByteArray(0) },
    )

    val image = AttachedFile(id = "id", file = platformFile, status = AttachedFile.Status.LOADING, uploadProgress = 75)

    TackleScreenPreview {
        Column {
            // Audio
            Box(modifier = Modifier.width(width = 420.dp).padding(all = 8.dp)) {
                AttachedFileContent(attachment = image.copy(file = platformFile.copy(mimeType = "audio")))
            }
            // Video
            Box(modifier = Modifier.width(width = 420.dp).padding(all = 8.dp)) {
                AttachedFileContent(attachment = image.copy(file = platformFile.copy(mimeType = "video")))
            }
            // Unknown
            Box(modifier = Modifier.width(width = 420.dp).padding(all = 8.dp)) {
                AttachedFileContent(attachment = image.copy(file = platformFile.copy(mimeType = "abc")))
            }
        }
    }
}
