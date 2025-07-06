package com.sedsoftware.tackle.compose.ui.status.content.media

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.clickableOnce
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.compose.ui.status.StatusContentLabel
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.domain.model.MediaAttachment
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.status_image_alt
import tackle.shared.compose.generated.resources.status_sensitive_hide
import tackle.shared.compose.generated.resources.status_sensitive_image
import tackle.shared.compose.generated.resources.status_sensitive_show

@Composable
internal fun StatusAttachmentImage(
    attachment: MediaAttachment,
    hasSensitiveContent: Boolean,
    hideSensitiveContent: Boolean,
    onImageClick: () -> Unit,
    onImageAltClick: () -> Unit,
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
                .clickableOnce(onClick = onImageClick)
                .fillMaxWidth(),
        )

        if (attachment.description.isNotEmpty()) {
            StatusContentLabel(
                text = stringResource(resource = Res.string.status_image_alt),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(all = 8.dp)
                    .clickable(onClick = onImageAltClick),
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

        if (hasSensitiveContent) {
            StatusContentLabel(
                text = stringResource(resource = Res.string.status_sensitive_image),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(all = 8.dp),
            )
        }
    }
}
