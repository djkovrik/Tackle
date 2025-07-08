package com.sedsoftware.tackle.compose.ui.status.content.media

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.clickableOnce
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.compose.ui.status.StatusContentLabel
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.compose.widget.TackleImageLoading
import com.sedsoftware.tackle.domain.model.MediaAttachment
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.status_image_alt
import tackle.shared.compose.generated.resources.status_sensitive_content
import tackle.shared.compose.generated.resources.status_sensitive_hide
import tackle.shared.compose.generated.resources.status_sensitive_show

@Composable
internal fun StatusAttachmentSingleImage(
    displayedAttachment: MediaAttachment,
    hasSensitiveContent: Boolean,
    hideSensitiveContent: Boolean,
    onImageClick: () -> Unit,
    onImageAltClick: (String) -> Unit,
    onSensitiveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayedAttachmentParams: TackleImageParams = remember {
        TackleImageParams(
            blurhash = displayedAttachment.blurhash,
            ratio = displayedAttachment.meta?.small?.aspect
                ?: displayedAttachment.meta?.original?.aspect
                ?: 1f,
        )
    }

    Box(modifier = modifier.clip(shape = MaterialTheme.shapes.extraSmall)) {
        Crossfade(targetState = !hideSensitiveContent) { showImage: Boolean ->
            if (showImage) {
                TackleImage(
                    imageUrl = displayedAttachment.previewUrl,
                    imageParams = displayedAttachmentParams,
                    contentDescription = displayedAttachment.description,
                    modifier = modifier
                        .clickableOnce(onClick = onImageClick)
                        .fillMaxWidth(),
                )
            } else {
                TackleImageLoading(
                    blurhash = displayedAttachmentParams.blurhash,
                    modifier = modifier
                        .aspectRatio(ratio = displayedAttachmentParams.ratio)
                        .fillMaxWidth(),
                )
            }
        }

        if (displayedAttachment.description.isNotEmpty()) {
            StatusContentLabel(
                text = stringResource(resource = Res.string.status_image_alt),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(all = 8.dp)
                    .clickableOnce { onImageAltClick.invoke(displayedAttachment.description) },
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
                    .clickableOnce(onClick = onSensitiveClick),
            )
        }

        if (hideSensitiveContent) {
            StatusContentLabel(
                text = stringResource(resource = Res.string.status_sensitive_content),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(all = 8.dp),
            )
        }
    }
}
