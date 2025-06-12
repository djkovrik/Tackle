package com.sedsoftware.tackle.compose.ui.editor.child.details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.editor.details.model.AttachmentParams
import com.sedsoftware.tackle.utils.extension.focusToOffset
import com.sedsoftware.tackle.utils.extension.offsetToFocus
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
internal fun AttachedImageFocusSelector(
    url: String,
    focus: Pair<Float, Float>,
    imageParams: AttachmentParams,
    modifier: Modifier = Modifier,
    onFocusChange: (Float, Float) -> Unit = { _, _ -> },
) {
    val indicatorSize: Dp = 48.dp

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(
                ratio = imageParams.ratio
            )
    ) {
        val parentWidth: Int = remember { constraints.maxWidth }
        val parentHeight: Int = remember { constraints.maxHeight }
        val middlePointX: Float = remember { parentWidth / 2f }
        val middlePointY: Float = remember { parentHeight / 2f }

        val indicatorWidthPx: Float = with(LocalDensity.current) { indicatorSize.toPx() }
        var offsetX: Float by remember { mutableStateOf(0f) }
        var offsetY: Float by remember { mutableStateOf(0f) }

        val initialOffset = focus.focusToOffset(middlePointX, middlePointY)
        offsetX = initialOffset.first
        offsetY = initialOffset.second

        TackleImage(
            data = url,
            contentDescription = null,
            params = TackleImageParams(
                blurhash = imageParams.blurhash,
                width = imageParams.width,
                height = imageParams.height,
                ratio = imageParams.ratio,
            ),
            modifier = modifier
                .fillMaxSize()
                .clip(shape = MaterialTheme.shapes.extraSmall),
        )

        FocusPointerIndicator(
            size = indicatorSize,
            modifier = Modifier
                .offset { IntOffset(x = offsetX.roundToInt(), y = offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change: PointerInputChange, dragAmount: Offset ->
                        change.consume()

                        offsetX = (offsetX + dragAmount.x).coerceIn(
                            minimumValue = -middlePointX + indicatorWidthPx,
                            maximumValue = middlePointX - indicatorWidthPx,
                        )

                        offsetY = (offsetY + dragAmount.y).coerceIn(
                            minimumValue = -middlePointY + indicatorWidthPx,
                            maximumValue = middlePointY - indicatorWidthPx,
                        )

                        val newFocus = (offsetX to offsetY).offsetToFocus(middlePointX, middlePointY)
                        onFocusChange.invoke(newFocus.first, newFocus.second)
                    }
                }
        )
    }
}

@Composable
internal fun FocusPointerIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.surfaceBright,
) {
    Box(
        modifier = modifier
            .size(size = size)
            .background(
                color = color.copy(
                    alpha = 0.5f,
                ),
                shape = CircleShape,
            )
            .clip(shape = CircleShape)
            .border(
                width = 1.dp,
                color = color.copy(
                    alpha = 0.75f
                ),
                shape = CircleShape
            ),
    )
}

@Preview
@Composable
private fun FocusPointerIndicatorPreview() {
    TackleScreenPreview {
        Surface(color = MaterialTheme.colorScheme.inverseSurface) {
            FocusPointerIndicator(modifier = Modifier.padding(all = 4.dp))
        }
    }
}
