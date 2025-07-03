package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.utils.extension.orZero
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.attachment_done
import tackle.shared.compose.generated.resources.status_image_alt
import tackle.shared.compose.generated.resources.status_sensitive_hide
import tackle.shared.compose.generated.resources.status_sensitive_image
import tackle.shared.compose.generated.resources.status_sensitive_show

@Composable
internal fun StatusAttachmentsImageList(
    attachments: List<MediaAttachment>,
    hasSensitiveContent: Boolean,
    modifier: Modifier = Modifier,
    onImageAltClick: () -> Unit = {},
) {
    var selectedAttachmentIndex: Int by remember { mutableStateOf(0) }
    val displayedAttachment: MediaAttachment = attachments[selectedAttachmentIndex]
    var showSensitiveInfo: Boolean by remember { mutableStateOf(hasSensitiveContent) }

    Column(modifier = modifier) {
        Box {
            TackleImage(
                data = displayedAttachment.previewUrl,
                contentDescription = displayedAttachment.description,
                params = TackleImageParams(
                    blurhash = displayedAttachment.blurhash,
                    width = displayedAttachment.meta?.original?.width.orZero(),
                    height = displayedAttachment.meta?.original?.height.orZero(),
                    ratio = displayedAttachment.meta?.original?.aspect ?: 1f,
                ),
                sensitive = showSensitiveInfo,
                modifier = Modifier.fillMaxWidth(),
            )

            if (displayedAttachment.description.isNotEmpty()) {
                ImageOverlayLabel(
                    text = stringResource(resource = Res.string.status_image_alt),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(all = 8.dp)
                        .clickable(onClick = onImageAltClick),
                )
            }

            if (hasSensitiveContent) {
                ImageOverlayLabel(
                    text = stringResource(
                        resource = if (showSensitiveInfo) {
                            Res.string.status_sensitive_show
                        } else {
                            Res.string.status_sensitive_hide
                        }
                    ),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(all = 8.dp)
                        .clickable { showSensitiveInfo = !showSensitiveInfo },
                )
            }

            if (showSensitiveInfo) {
                ImageOverlayLabel(
                    text = stringResource(resource = Res.string.status_sensitive_image),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(all = 8.dp),
                )
            }
        }

        AnimatedVisibility(visible = attachments.size > 1) {
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
                            width = item.meta?.small?.width.orZero(),
                            height = item.meta?.small?.height.orZero(),
                            ratio = item.meta?.small?.aspect ?: 1f,
                        ),
                        onSelect = { selectedAttachmentIndex = index },
                        sensitive = showSensitiveInfo,
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
    onSelect: () -> Unit,
    sensitive: Boolean = false,
    params: TackleImageParams? = null,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.extraSmall)
            .border(
                width = 1.dp,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
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
            sensitive = sensitive,
            modifier = Modifier
                .height(height = 64.dp)
                .aspectRatio(ratio = params?.ratio ?: 1f)
        )

        AnimatedVisibility(
            visible = selected,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
        ) {
            Icon(
                painter = painterResource(resource = Res.drawable.attachment_done),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(size = 24.dp)
            )
        }
    }
}

@Composable
private fun ImageOverlayLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.inverseSurface,
                    shape = MaterialTheme.shapes.extraSmall,
                )
                .padding(all = 4.dp),
        )
    }
}
