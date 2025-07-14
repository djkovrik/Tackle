package com.sedsoftware.tackle.compose.ui.status.content.media

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.brys.compose.blurhash.BlurHashImage
import com.sedsoftware.tackle.compose.extension.clickableOnce
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.compose.ui.SharedTransitionScopes.LocalNavAnimatedVisibilityScope
import com.sedsoftware.tackle.compose.ui.SharedTransitionScopes.LocalSharedTransitionScope
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
import tackle.shared.compose.generated.resources.status_sensitive_content
import tackle.shared.compose.generated.resources.status_sensitive_hide
import tackle.shared.compose.generated.resources.status_sensitive_show

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
internal fun StatusAttachmentVideo(
    displayedAttachment: MediaAttachment,
    hasSensitiveContent: Boolean,
    hideSensitiveContent: Boolean,
    onVideoClick: () -> Unit,
    onVideoAltClick: (String) -> Unit,
    onSensitiveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayedAttachmentParams: TackleImageParams = remember {
        TackleImageParams(
            blurhash = displayedAttachment.blurhash,
            ratio = displayedAttachment.meta?.small?.aspect ?: 1f,
        )
    }

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No SharedElementScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No AnimatedVisibility found")

    with(sharedTransitionScope) {
        Box(modifier = modifier
            .sharedBounds(
                rememberSharedContentState(key = displayedAttachment.id),
                animatedVisibilityScope = animatedVisibilityScope,
                enter = fadeIn(),
                exit = fadeOut(),
                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(),
            )
            .clip(shape = MaterialTheme.shapes.extraSmall)
        ) {
            Crossfade(targetState = !hideSensitiveContent) { showImage: Boolean ->
                if (showImage) {
                    TackleImage(
                        imageUrl = displayedAttachment.previewUrl,
                        imageParams = displayedAttachmentParams,
                        contentDescription = displayedAttachment.description,
                        modifier = modifier.fillMaxWidth(),
                    )
                } else {
                    BlurHashImage(
                        hash = displayedAttachmentParams.blurhash,
                        contentDescription = "",
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
                        .clickable { onVideoAltClick.invoke(displayedAttachment.description) }
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

            when {
                displayedAttachment.type == MediaAttachmentType.GIF || displayedAttachment.type == MediaAttachmentType.GIFV ->
                    StatusContentLabel(
                        text = stringResource(resource = Res.string.status_image_gif),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(all = 8.dp),
                    )

                displayedAttachment.meta?.original?.duration != null -> {
                    displayedAttachment.meta?.original?.duration?.let { videoDuration: Float ->
                        StatusContentLabel(
                            text = videoDuration.toVideoDuration(),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(all = 8.dp),
                        )
                    }
                }
            }

            if (hideSensitiveContent) {
                StatusContentLabel(
                    text = stringResource(resource = Res.string.status_sensitive_content),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(all = 8.dp),
                )
            } else {
                IconButton(
                    onClick = onVideoClick,
                    modifier = modifier
                        .clip(shape = CircleShape)
                        .background(
                            color = MaterialTheme.colorScheme.inverseSurface.copy(
                                alpha = 0.8f
                            )
                        )
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
    }
}
