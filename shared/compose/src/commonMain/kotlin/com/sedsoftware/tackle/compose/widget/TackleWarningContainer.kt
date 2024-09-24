package com.sedsoftware.tackle.compose.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.ui.editor.child.warning.content.createStripedBrush

@Composable
internal fun TackleWarningContainer(
    modifier: Modifier = Modifier,
    indicatorColor: Color = MaterialTheme.colorScheme.tertiary,
    indicatorWidth: Dp = 8.dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val width: Float = indicatorWidth.value * density

                // left
                drawLine(
                    brush = createStripedBrush(
                        stripeColor = indicatorColor,
                        stripeWidth = 4.dp,
                        stripeToGapRatio = 1f,
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = width,
                )
                // right
                drawLine(
                    brush = createStripedBrush(
                        stripeColor = indicatorColor,
                        stripeWidth = 4.dp,
                        stripeToGapRatio = 1f,
                    ),
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = width,
                )
            }
    ) {
        content()
    }
}
