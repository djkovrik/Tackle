@file:Suppress("MagicNumber")

package com.sedsoftware.tackle.compose.core

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
internal fun InfiniteTransition.fractionTransition(
    initialValue: Float,
    targetValue: Float,
    fraction: Int = 1,
    durationMillis: Int,
    delayMillis: Int = 0,
    offsetMillis: Int = 0,
    repeatMode: RepeatMode = RepeatMode.Restart,
    easing: Easing = FastOutSlowInEasing,
): State<Float> {
    return animateFloat(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                this.durationMillis = durationMillis
                this.delayMillis = delayMillis
                initialValue at 0 using easing
                when (fraction) {
                    1 -> {
                        targetValue at durationMillis using easing
                    }

                    2 -> {
                        targetValue / fraction at durationMillis / fraction using easing
                        targetValue at durationMillis using easing
                    }

                    3 -> {
                        targetValue / fraction at durationMillis / fraction using easing
                        targetValue / fraction * 2 at durationMillis / fraction * 2 using easing
                        targetValue at durationMillis using easing
                    }

                    4 -> {
                        targetValue / fraction at durationMillis / fraction using easing
                        targetValue / fraction * 2 at durationMillis / fraction * 2 using easing
                        targetValue / fraction * 3 at durationMillis / fraction * 3 using easing
                        targetValue at durationMillis using easing
                    }
                }
            },
            repeatMode,
            StartOffset(offsetMillis)
        )
    )
}

val EaseInOut = CubicBezierEasing(0.42f, 0f, 0.58f, 1f)
