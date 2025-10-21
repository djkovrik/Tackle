package com.sedsoftware.tackle.compose.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.custom.RotatingProgress
import com.sedsoftware.tackle.utils.extension.roundToDecimals

@Composable
fun TackleImageProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    progressSize: Dp = 32.dp,
    indicatorColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    val progressVisible: Boolean = progress > 0f && progress < 1f
    val progressValue: Float = progress.roundToDecimals(2)

    val progressAlpha: Float by animateFloatAsState(
        targetValue = if (progressVisible) 1f else 0f,
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
    }
}
