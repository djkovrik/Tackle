package com.sedsoftware.tackle.compose.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
internal fun LoadingDotsText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.primary,
    cycleDuration: Int = 1000,
) {
    val dotsTransition = rememberInfiniteTransition(label = "Text dots")

    val dotsCount by dotsTransition.animateValue(
        initialValue = 1,
        targetValue = 4,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = cycleDuration,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Dots animation"
    )

    val alphaTransition = rememberInfiniteTransition(label = "Text alpha")

    val alpha by alphaTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = cycleDuration / AlphaDurationDivider,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "Alpha animation",
    )

    Row(modifier = modifier.alpha(alpha)) {
        Text(
            text = text,
            color = color,
            style = style,
            maxLines = 1,
        )
        Text(
            text = ".".repeat(dotsCount),
            color = color,
            style = style,
            maxLines = 1,
            modifier = Modifier.width(width = 24.dp),
        )
    }
}

private const val AlphaDurationDivider = 3
