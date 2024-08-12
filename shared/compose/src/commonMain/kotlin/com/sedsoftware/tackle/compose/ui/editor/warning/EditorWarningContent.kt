package com.sedsoftware.tackle.compose.ui.editor.warning

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.editor.warning.content.createStripedBrush
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_close
import tackle.shared.compose.generated.resources.editor_warning_hint

@Composable
internal fun EditorWarningContent(
    text: String,
    modifier: Modifier = Modifier,
    onTextInput: (String) -> Unit = {},
    onTextClear: () -> Unit = {},
) {
    val indicatorColor: Color = MaterialTheme.colorScheme.error
    val indicatorWidth: Dp = 8.dp
    val cornerRadius: Dp = 6.dp

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
        TextField(
            value = text,
            onValueChange = onTextInput,
            maxLines = 3,
            textStyle = MaterialTheme.typography.bodyLarge,
            trailingIcon = {
                AnimatedVisibility(
                    visible = text.isNotBlank(),
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                ) {
                    Icon(
                        painterResource(resource = Res.drawable.editor_close),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .size(size = 24.dp)
                            .clickable(onClick = onTextClear),
                    )
                }
            },
            placeholder = {
                Text(
                    text = stringResource(resource = Res.string.editor_warning_hint),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f,
                    ),
                )
            },
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(size = cornerRadius)),
        )
    }
}

@Preview
@Composable
private fun EditorWarningComponentEmptyPreview() {
    TackleScreenPreview {
        EditorWarningContent(
            text = "",
            modifier = Modifier.padding(all = 2.dp)
        )
    }
}


@Preview
@Composable
private fun EditorWarningComponentSingleLinePreview() {
    TackleScreenPreview {
        EditorWarningContent(
            text = "abcdef",
            modifier = Modifier.padding(all = 2.dp)
        )
    }
}
