package com.sedsoftware.tackle.compose.ui.status.content.media

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.clickableOnce
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.compose.widget.TackleImageLoading
import com.sedsoftware.tackle.domain.model.MediaAttachment
import org.jetbrains.compose.resources.painterResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.attachment_done

@Composable
internal fun StatusAttachmentImageGalleryItem(
    galleryItem: MediaAttachment,
    selected: Boolean,
    sensitive: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val galleryItemParams: TackleImageParams = remember {
        TackleImageParams(
            blurhash = galleryItem.blurhash,
            ratio = galleryItem.meta?.small?.aspect
                ?: galleryItem.meta?.original?.aspect
                ?: 1f,
        )
    }

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
            .clickableOnce(onClick = onSelect)
    ) {
        Crossfade(targetState = !sensitive) { showImage: Boolean ->
            if (showImage) {
                TackleImage(
                    imageUrl = galleryItem.url,
                    imageParams = galleryItemParams,
                    contentDescription = null,
                    modifier = Modifier
                        .height(height = 64.dp)
                        .aspectRatio(ratio = galleryItemParams.ratio)
                )
            } else {
                TackleImageLoading(
                    blurhash = galleryItemParams.blurhash,
                    modifier = modifier
                        .height(height = 64.dp)
                        .aspectRatio(ratio = galleryItemParams.ratio),
                )
            }
        }

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
