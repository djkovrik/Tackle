package com.sedsoftware.tackle.compose.ui.status.content.media

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.compose.ui.status.StatusContentLabel
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.utils.extension.toVideoDuration
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.status_image_alt
import tackle.shared.compose.generated.resources.status_image_gif
import tackle.shared.compose.generated.resources.status_play
import tackle.shared.compose.generated.resources.status_sensitive_hide
import tackle.shared.compose.generated.resources.status_sensitive_show

@Composable
internal fun StatusAttachmentVideo(
    attachment: MediaAttachment,
    hasSensitiveContent: Boolean,
    hideSensitiveContent: Boolean,
    onVideoClick: () -> Unit,
    onVideoAltClick: (String) -> Unit,
    onSensitiveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box {
        TackleImage(
            imageUrl = attachment.previewUrl,
            contentDescription = attachment.description,
            params = TackleImageParams(
                blurhash = attachment.blurhash,
                ratio = attachment.meta?.small?.aspect
                    ?: attachment.meta?.original?.aspect
                    ?: 0f,
            ),
            sensitive = hideSensitiveContent,
            modifier = modifier
                .clip(shape = MaterialTheme.shapes.extraSmall)
                .fillMaxWidth(),
        )

        if (attachment.description.isNotEmpty()) {
            StatusContentLabel(
                text = stringResource(resource = Res.string.status_image_alt),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(all = 8.dp)
                    .clickable { onVideoAltClick.invoke(attachment.description) }
            )
        }

        if (hasSensitiveContent) {
            StatusContentLabel(
                text = stringResource(
                    resource = if (hideSensitiveContent) {
                        Res.string.status_sensitive_show
                    } else {
                        Res.string.status_sensitive_hide
                    }
                ),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(all = 8.dp)
                    .clickable(onClick = onSensitiveClick),
            )
        }

        when {
            attachment.type == MediaAttachmentType.GIF || attachment.type == MediaAttachmentType.GIFV ->
                StatusContentLabel(
                    text = stringResource(resource = Res.string.status_image_gif),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(all = 8.dp),
                )

            attachment.meta?.original?.duration != null -> {
                attachment.meta?.original?.duration?.let { videoDuration: Float ->
                    StatusContentLabel(
                        text = videoDuration.toVideoDuration(),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(all = 8.dp),
                    )
                }
            }
        }

        IconButton(
            onClick = onVideoClick,
            modifier = modifier
                .clip(shape = CircleShape)
                .background(color = MaterialTheme.colorScheme.inverseSurface.copy(
                    alpha = 0.8f
                ))
                .align(Alignment.Center),
        ) {
            Icon(
                painter = painterResource(resource = Res.drawable.status_play),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.inverseOnSurface.copy(
                    alpha = 0.8f
                ),
                modifier = Modifier.padding(all = 6.dp),
            )
        }
    }
}
