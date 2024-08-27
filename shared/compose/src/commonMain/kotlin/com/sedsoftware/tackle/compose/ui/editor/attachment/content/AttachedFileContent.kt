package com.sedsoftware.tackle.compose.ui.editor.attachment.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.getTypeTitle
import com.sedsoftware.tackle.compose.extension.hasError
import com.sedsoftware.tackle.compose.extension.hasProcessingVideo
import com.sedsoftware.tackle.compose.extension.hasVideoThumbnail
import com.sedsoftware.tackle.compose.model.AttachedFilePreviewType
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.utils.extension.isImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_attachment_failed
import tackle.shared.compose.generated.resources.editor_delete
import tackle.shared.compose.generated.resources.editor_done
import tackle.shared.compose.generated.resources.preview_sample

@Composable
internal fun AttachedFileContent(
    attachment: AttachedFile,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    onDelete: () -> Unit = {},
    previewImage: @Composable (() -> Unit)? = null,
) {
    var imageData: ByteArray by remember { mutableStateOf(ByteArray(0)) }
    var progress: Float by remember { mutableStateOf(0.0f) }
    var showProcessingLabel: Boolean by remember { mutableStateOf(false) }

    val animatedProgress: Float by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
    )

    val animatedProgressAlpha: Float by animateFloatAsState(
        targetValue = if (attachment.status == AttachedFile.Status.LOADED) 0.5f else 1f,
        animationSpec = tween(easing = LinearEasing)
    )

    val animatedProcessingLabelAlpha: Float by animateFloatAsState(
        targetValue = if (showProcessingLabel) 0.75f else 0f,
        animationSpec = tween(easing = LinearEasing)
    )

    val animatedDoneIconState: Float by animateFloatAsState(
        targetValue = if (attachment.status == AttachedFile.Status.LOADED) 1f else 0f,
        animationSpec = tween(
            easing = LinearEasing,
            durationMillis = DONE_ICON_ANIM_DURATION,
        )
    )

    progress = attachment.uploadProgress

    LaunchedEffect(attachment.id) {
        if (attachment.file.isImage) {
            imageData = attachment.file.readBytes.invoke()
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
            AnimatedContent(
                targetState = when {
                    attachment.file.isImage && previewImage != null -> AttachedFilePreviewType.PREVIEW_STUB
                    attachment.file.isImage && previewImage == null -> AttachedFilePreviewType.LOCAL_IMAGE
                    attachment.hasVideoThumbnail -> AttachedFilePreviewType.VIDEO_THUMBNAIL
                    else -> AttachedFilePreviewType.GENERIC_STUB
                },
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
            ) { state: AttachedFilePreviewType ->
                when (state) {
                    AttachedFilePreviewType.PREVIEW_STUB -> {
                        previewImage?.invoke()
                    }

                    AttachedFilePreviewType.LOCAL_IMAGE -> {
                        AttachedFileImageThumbnail(
                            imageData = imageData,
                        )
                    }

                    AttachedFilePreviewType.VIDEO_THUMBNAIL -> {
                        AttachedFileVideoThumbnail(
                            url = attachment.serverCopy?.previewUrl.orEmpty(),
                        )
                    }

                    AttachedFilePreviewType.GENERIC_STUB -> {
                        AttachedFileContentGeneric(
                            attachment = attachment,
                        )
                    }
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

            // Attachment is being processed
            AttachedFileProcessing(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .align(alignment = Alignment.BottomEnd)
                    .alpha(alpha = animatedProcessingLabelAlpha)
            )

            showProcessingLabel = attachment.hasProcessingVideo
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
                progress = { animatedProgress },
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(alpha = animatedProgressAlpha)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            // Info bar
            // (size + media type) --- buttons: edit - retry - delete
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(resource = attachment.getTypeTitle()),
                            color = if (attachment.hasError) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                        )

                        Image(
                            painter = painterResource(resource = Res.drawable.editor_done),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimaryContainer),
                            modifier = Modifier
                                .size(size = 14.dp)
                                .alpha(alpha = animatedDoneIconState)
                                .scale(scale = animatedDoneIconState)
                        )
                    }
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

private const val DONE_ICON_ANIM_DURATION = 150

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

    val image = AttachedFile(id = "id", file = platformFile, status = AttachedFile.Status.LOADING, uploadProgress = 0.75f)

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
                AttachedFileContent(attachment = image.copy(status = AttachedFile.Status.ERROR))
            }
            // Loaded
            Box(modifier = Modifier.width(width = 420.dp).padding(all = 8.dp)) {
                AttachedFileContent(attachment = image.copy(status = AttachedFile.Status.LOADED)) {
                    AttachmentPreviewImageStub()
                }
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

    val image = AttachedFile(id = "id", file = platformFile, status = AttachedFile.Status.LOADING, uploadProgress = 0.75f)

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

@Preview
@Composable
private fun AttachedVideoProcessingPreview() {
    val platformFile = PlatformFileWrapper(
        name = "test.mp4",
        extension = "mp4",
        path = "",
        mimeType = "video/mpeg",
        size = 0L,
        sizeLabel = "123 Mb",
        readBytes = { ByteArray(0) },
    )

    val video = AttachedFile(
        id = "id",
        file = platformFile,
        status = AttachedFile.Status.LOADED,
        uploadProgress = 0.1f,
        serverCopy = MediaAttachment(
            id = "id",
            type = MediaAttachmentType.VIDEO,
            url = "",
            previewUrl = "",
            remoteUrl = "",
            description = "",
            blurhash = "",
            meta = null,
        )
    )


    TackleScreenPreview {
        AttachedFileContent(attachment = video)
    }
}
