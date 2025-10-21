package com.sedsoftware.tackle.compose.custom

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode

@Composable
internal fun shimmerEffectBrush(): ShaderBrush {
    val infiniteTransition: InfiniteTransition = rememberInfiniteTransition(label = "Shimmer transition")

    val offset: Float by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = ANIMATION_DURATION_MS),
        ),
        label = "Shimmer offset",
    )
    return remember(offset) {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val widthOffset = size.width * offset
                val heightOffset = size.height * offset
                return LinearGradientShader(
                    colors = listOf(
                        Color.LightGray.copy(alpha = 0.4f),
                        Color.LightGray.copy(alpha = 0.6f),
                        Color.LightGray.copy(alpha = 0.4f),
                    ),
                    from = Offset(x = widthOffset, y = heightOffset),
                    to = Offset(x = widthOffset + size.width, y = heightOffset + size.height),
                    tileMode = TileMode.Mirror,
                )
            }
        }
    }
}

private const val ANIMATION_DURATION_MS = 1_000
