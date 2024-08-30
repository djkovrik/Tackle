package com.sedsoftware.tackle.compose.ui.editor.child.warning.content

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

internal fun Density.createStripedBrush(
    stripeColor: Color,
    stripeWidth: Dp,
    stripeToGapRatio: Float,
): Brush {
    val stripeWidthPx = stripeWidth.toPx()
    val stripeGapWidthPx = stripeWidthPx / stripeToGapRatio
    val brushSizePx = stripeGapWidthPx + stripeWidthPx
    val stripeStart = stripeGapWidthPx / brushSizePx

    return Brush.linearGradient(
        stripeStart to Color.Transparent,
        stripeStart to stripeColor,
        start = Offset(0f, 0f),
        end = Offset(brushSizePx, brushSizePx),
        tileMode = TileMode.Repeated,
    )
}
