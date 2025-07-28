package com.sedsoftware.tackle.compose.ui.editor.child.attachment.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.platform.toImageBitmap
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile.Status.PENDING
import com.sedsoftware.tackle.utils.extension.isAudio
import com.sedsoftware.tackle.utils.extension.isImage
import com.sedsoftware.tackle.utils.extension.isVideo
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_file_audio
import tackle.shared.compose.generated.resources.editor_file_image
import tackle.shared.compose.generated.resources.editor_file_unknown
import tackle.shared.compose.generated.resources.editor_file_video

@Composable
internal fun AttachedFileImageThumbnail(
    imageData: ByteArray,
    modifier: Modifier = Modifier,
) {
    var imageBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    LaunchedEffect(imageData) {
        if (imageData.isNotEmpty()) {
            imageBitmap = imageData.toImageBitmap()
        }
    }

    imageBitmap?.let { bitmap ->
        Image(
            bitmap = bitmap,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier,
        )
    } ?: AttachedFilePlaceholder(
        resource = Res.drawable.editor_file_image,
        modifier = modifier,
    )
}

@Composable
internal fun AttachedFileVideoThumbnail(
    url: String,
    modifier: Modifier = Modifier,
) {
    if (url.isNotEmpty()) {
        TackleImage(
            imageUrl = url,
            imageParams = null,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.fillMaxSize(),
        )
    }
}

@Composable
internal fun AttachedFileContentGeneric(
    attachment: AttachedFile,
    modifier: Modifier = Modifier,
) {
    AttachedFilePlaceholder(
        resource = when {
            attachment.file.isAudio -> Res.drawable.editor_file_audio
            attachment.file.isImage -> Res.drawable.editor_file_image
            attachment.file.isVideo -> Res.drawable.editor_file_video
            else -> Res.drawable.editor_file_unknown
        },
        modifier = modifier,
    )
}

@Composable
internal fun AttachedFilePlaceholder(
    resource: DrawableResource,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.75f
                )
            )
    ) {
        val halfMultiplier = 0.5f
        val imageSize = (constraints.maxHeight * halfMultiplier).dp

        Image(
            painter = painterResource(resource = resource),
            contentScale = ContentScale.Fit,
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.outline.copy(
                    alpha = 0.5f,
                )
            ),
            modifier = Modifier.size(size = imageSize),
        )
    }
}

@Preview
@Composable
private fun AttachedFileContentPreview() {
    val platformFile = PlatformFileWrapper("", "", "", "", 0L, "") { ByteArray(0) }
    val previewHeight = 220.dp

    TackleScreenPreview {
        Column {
            Box(modifier = Modifier.height(height = previewHeight).padding(all = 8.dp)) {
                AttachedFileContentGeneric(
                    attachment = AttachedFile("id", platformFile.copy(mimeType = "audio"), PENDING)
                )
            }

            Box(modifier = Modifier.height(height = previewHeight).padding(all = 8.dp)) {
                AttachedFileContentGeneric(
                    attachment = AttachedFile("id", platformFile.copy(mimeType = "image"), PENDING)
                )
            }

            Box(modifier = Modifier.height(height = previewHeight).padding(all = 8.dp)) {
                AttachedFileContentGeneric(
                    attachment = AttachedFile("id", platformFile.copy(mimeType = "video"), PENDING)
                )
            }

            Box(modifier = Modifier.height(height = previewHeight).padding(all = 8.dp)) {
                AttachedFileContentGeneric(
                    attachment = AttachedFile("id", platformFile, PENDING)
                )
            }
        }
    }
}
