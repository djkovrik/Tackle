package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.PreviewStubs
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.utils.extension.orZero
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StatusAttachmentsImageList(
    attachments: List<MediaAttachment>,
    modifier: Modifier = Modifier,
) {
    var selectedAttachmentIndex: Int by remember { mutableStateOf(0) }
    val displayedAttachment: MediaAttachment = attachments[selectedAttachmentIndex]

    Column(modifier = modifier) {
        TackleImage(
            data = displayedAttachment.url,
            contentDescription = displayedAttachment.description,
            params = TackleImageParams(
                blurhash = displayedAttachment.blurhash,
                width = displayedAttachment.meta?.width.orZero(),
                height = displayedAttachment.meta?.height.orZero(),
                ratio = displayedAttachment.meta?.aspect ?: 1f,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        AnimatedVisibility(visible = attachments.size > 1,) {
            LazyRow(modifier = Modifier.padding(all = 4.dp)) {
                itemsIndexed(
                    items = attachments,
                    key = { index: Int, _ -> index }
                ) { index: Int, item: MediaAttachment ->

                    StatusAttachmentGalleryItem(
                        url = item.previewUrl,
                        selected = index == selectedAttachmentIndex,
                        params = TackleImageParams(
                            blurhash = item.blurhash,
                            width = item.meta?.width.orZero(),
                            height = item.meta?.height.orZero(),
                            ratio = item.meta?.aspect ?: 1f,
                        ),
                        onSelect = { selectedAttachmentIndex = index },
                    )

                    Spacer(modifier = Modifier.width(width = 4.dp))
                }
            }
        }
    }
}

@Composable
private fun StatusAttachmentGalleryItem(
    url: String,
    selected: Boolean,
    params: TackleImageParams? = null,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.extraSmall)
            .border(
                width = 1.dp,
                color = if (selected) {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.1f)
                },
                shape = MaterialTheme.shapes.extraSmall,
            )
            .clickable(onClick = onSelect)
    ) {
        TackleImage(
            data = url,
            contentDescription = null,
            params = params,
            modifier = Modifier
                .height(height = 64.dp)
                .aspectRatio(ratio = params?.ratio ?: 1f)
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
    StatusAttachmentsImageList(
        attachments = listOf(
            PreviewStubs.mediaAttachment.copy(type = MediaAttachmentType.IMAGE),
            PreviewStubs.mediaAttachment.copy(type = MediaAttachmentType.IMAGE),
            PreviewStubs.mediaAttachment.copy(type = MediaAttachmentType.IMAGE),
            PreviewStubs.mediaAttachment.copy(type = MediaAttachmentType.IMAGE),
        )
    )
}
