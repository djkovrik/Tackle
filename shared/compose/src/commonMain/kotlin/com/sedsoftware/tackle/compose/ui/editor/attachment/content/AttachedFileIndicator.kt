package com.sedsoftware.tackle.compose.ui.editor.attachment.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile.Status
import org.jetbrains.compose.resources.painterResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_close
import tackle.shared.compose.generated.resources.editor_done
import tackle.shared.compose.generated.resources.editor_retry

private const val ANIMATION_DURATION = 750

@Composable
internal fun AttachedFileIndicator(
    status: Status,
    uploadProgress: Int,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    onRetry: () -> Unit = {},
    indicatorSize: Dp = 48.dp,
) {
    val infiniteTransition: InfiniteTransition = rememberInfiniteTransition()

    val angle: Float by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size = indicatorSize)
            .clip(shape = CircleShape)
            .background(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {

        AnimatedVisibility(visible = status == Status.PENDING || status == Status.LOADING) {
            CircularProgressIndicator(
                progress = { uploadProgress / 100f },
                color = MaterialTheme.colorScheme.surface,
                strokeWidth = 4.dp,
                modifier = Modifier
                    .size(size = indicatorSize - 12.dp)
                    .rotate(degrees = angle),
            )
        }

        AnimatedContent(
            targetState = status,
            transitionSpec = {
                scaleIn(animationSpec = tween(ANIMATION_DURATION)) + fadeIn(animationSpec = tween(ANIMATION_DURATION)) togetherWith
                    scaleOut(animationSpec = tween(ANIMATION_DURATION)) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
            },
        ) { targetState: Status ->
            when (targetState) {
                Status.LOADING -> {
                    Icon(
                        painter = painterResource(resource = Res.drawable.editor_close),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .clickable(onClick = onDelete),
                    )
                }

                Status.LOADED -> {
                    Icon(
                        painter = painterResource(resource = Res.drawable.editor_done),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.padding(all = 8.dp),
                    )
                }

                Status.ERROR -> {
                    Icon(
                        painter = painterResource(resource = Res.drawable.editor_retry),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .clickable(onClick = onRetry)
                    )
                }

                Status.PENDING -> Unit
            }

        }
    }
}

@Preview
@Composable
private fun AttachedFileIndicatorsPreview() {
    TackleScreenPreview {
        Row {
            AttachedFileIndicator(
                status = Status.PENDING,
                uploadProgress = 0,
                indicatorSize = 64.dp,
            )

            Spacer(modifier = Modifier.width(width = 8.dp))

            AttachedFileIndicator(
                status = Status.LOADING,
                uploadProgress = 75,
                indicatorSize = 64.dp,
            )

            Spacer(modifier = Modifier.width(width = 8.dp))

            AttachedFileIndicator(
                status = Status.LOADED,
                uploadProgress = 100,
                indicatorSize = 64.dp,
            )

            Spacer(modifier = Modifier.width(width = 8.dp))

            AttachedFileIndicator(
                status = Status.ERROR,
                uploadProgress = 100,
                indicatorSize = 64.dp,
            )
        }
    }
}
