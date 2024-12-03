package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.PreviewStubs
import com.sedsoftware.tackle.compose.widget.TackleProgressIndicator
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_attachment_audio
import tackle.shared.compose.generated.resources.editor_attachment_unknown

@Composable
internal fun StatusAttachmentsFileList(
    attachments: List<MediaAttachment>,
    modifier: Modifier = Modifier,
    onDownloadClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    LazyRow(
        modifier = modifier,
    ) {
        itemsIndexed(
            items = attachments,
            key = { index: Int, _ -> index }
        ) { index: Int, item: MediaAttachment ->
            StatusAttachmentNoPreview(
                label = stringResource(
                    resource = if (item.type == MediaAttachmentType.AUDIO) {
                        Res.string.editor_attachment_audio
                    } else {
                        Res.string.editor_attachment_unknown
                    }
                ),
                index = index + 1,
                onDownloadClick = onDownloadClick,
                onCancelClick = onCancelClick,
                onDoneClick = onDoneClick,
            )

            Spacer(modifier = Modifier.width(width = 4.dp))
        }
    }
}

@Composable
private fun StatusAttachmentNoPreview(
    label: String,
    index: Int,
    modifier: Modifier = Modifier,
    onDownloadClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(size = 8.dp))
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .padding(start = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                )
                .size(size = 32.dp)
        ) {
            TackleProgressIndicator(
                progress = 0.5f,
                size = 32.dp,
                indicatorColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface,
                onDownloadClick = onDownloadClick,
                onCancelClick = onCancelClick,
                onDoneClick = onDoneClick,
            )
        }
        Text(
            text = "$label #$index",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(horizontal = 8.dp, vertical = 16.dp),
        )
    }
}

@Preview
@Composable
private fun StatusAttachmentsFileListPreviewLight() {
    TackleScreenPreview {
        StatusAttachmentsFileListPreviewContent()
    }
}

@Preview
@Composable
private fun StatusAttachmentsFileListPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        StatusAttachmentsFileListPreviewContent()
    }
}

@Composable
private fun StatusAttachmentsFileListPreviewContent() {
    StatusAttachmentsFileList(
        attachments = listOf(
            PreviewStubs.mediaAttachment.copy(type = MediaAttachmentType.AUDIO),
            PreviewStubs.mediaAttachment.copy(type = MediaAttachmentType.UNKNOWN),
            PreviewStubs.mediaAttachment.copy(type = MediaAttachmentType.AUDIO),
            PreviewStubs.mediaAttachment.copy(type = MediaAttachmentType.UNKNOWN),
        )
    )
}
