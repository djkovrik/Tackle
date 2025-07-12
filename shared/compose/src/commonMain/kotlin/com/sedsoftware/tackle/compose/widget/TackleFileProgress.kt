package com.sedsoftware.tackle.compose.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.custom.RotatingProgress
import com.sedsoftware.tackle.utils.extension.roundToDecimals
import org.jetbrains.compose.resources.painterResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.attachment_cancel
import tackle.shared.compose.generated.resources.attachment_done
import tackle.shared.compose.generated.resources.attachment_download

@Composable
internal fun TackleFileProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    progressSize: Dp = 32.dp,
    indicatorColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    onDownloadClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    val progressVisible: Boolean by derivedStateOf { progress > 0f && progress < 1f }
    val downloadVisible: Boolean by derivedStateOf { progress == 0f }
    val cancelVisible: Boolean by derivedStateOf { progress > 0f && progress < 1f }
    val doneVisible: Boolean by derivedStateOf { progress >= 1f }
    val progressValue: Float by derivedStateOf { progress.roundToDecimals(1) }

    val progressAlpha: Float by animateFloatAsState(
        targetValue = if (progressVisible) 1f else 0f,
        tween(easing = LinearEasing),
    )

    val downloadScale: Float by animateFloatAsState(
        targetValue = if (downloadVisible) 1f else 0f,
        tween(easing = LinearEasing),
    )

    val cancelScale: Float by animateFloatAsState(
        targetValue = if (cancelVisible) 1f else 0f,
        tween(easing = LinearEasing),
    )

    val doneScale: Float by animateFloatAsState(
        targetValue = if (doneVisible) 1f else 0f,
        tween(easing = LinearEasing),
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size = progressSize)
            .clip(shape = CircleShape)
            .background(color = containerColor)
            .alpha(alpha = progressAlpha),
    ) {
        RotatingProgress(
            progress = progressValue,
            active = progressVisible,
            color = indicatorColor,
            strokeWidth = progressSize * 0.06f,
            modifier = Modifier.size(size = progressSize * 0.85f),
        )

        Icon(
            painter = painterResource(resource = Res.drawable.attachment_download),
            contentDescription = null,
            tint = indicatorColor,
            modifier = Modifier
                .size(size = progressSize * 0.5f)
                .scale(scale = downloadScale)
                .clickable(onClick = onDownloadClick),
        )

        Icon(
            painter = painterResource(resource = Res.drawable.attachment_cancel),
            contentDescription = null,
            tint = indicatorColor,
            modifier = Modifier
                .size(size = progressSize * 0.5f)
                .scale(scale = cancelScale)
                .clickable(onClick = onCancelClick),
        )

        Icon(
            painter = painterResource(resource = Res.drawable.attachment_done),
            contentDescription = null,
            tint = indicatorColor,
            modifier = Modifier
                .size(size = progressSize * 0.5f)
                .scale(scale = doneScale)
                .clickable(onClick = onDoneClick),
        )
    }
}
