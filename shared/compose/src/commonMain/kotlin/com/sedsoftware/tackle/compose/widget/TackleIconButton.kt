package com.sedsoftware.tackle.compose.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.alsoIf
import com.sedsoftware.tackle.compose.extension.clickableOnce
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_language
import tackle.shared.compose.generated.resources.editor_send

@Composable
internal fun TackleIconButton(
    iconRes: DrawableResource,
    modifier: Modifier = Modifier,
    additionalText: String = "",
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    borderWidth: Dp = 0.dp,
    onClick: () -> Unit = {},
) {
    val animatedAlpha: Float by animateFloatAsState(
        targetValue = if (enabled) ALPHA_ENABLED else ALPHA_DISABLED,
        animationSpec = tween(
            easing = LinearEasing,
            durationMillis = ALPHA_ANIM_DURATION,
        )
    )

    if (additionalText.isNotEmpty()) {
        IconButtonWithText(
            text = additionalText,
            iconRes = iconRes,
            modifier = modifier,
            enabled = enabled,
            containerColor = containerColor,
            contentColor = contentColor,
            borderWidth = borderWidth,
            animatedAlpha = animatedAlpha,
            onClick = onClick,
        )
    } else {
        IconButton(
            iconRes = iconRes,
            modifier = modifier,
            enabled = enabled,
            containerColor = containerColor,
            contentColor = contentColor,
            borderWidth = borderWidth,
            animatedAlpha = animatedAlpha,
            onClick = onClick,
        )
    }
}

@Composable
private fun IconButton(
    iconRes: DrawableResource,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    borderWidth: Dp = 0.dp,
    animatedAlpha: Float = 1f,
    onClick: () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size = BUTTON_SIZE.dp)
            .clip(shape = CircleShape)
            .background(
                color = if (borderWidth != 0.dp) Color.Transparent else containerColor,
                shape = CircleShape
            )
            .border(
                width = borderWidth,
                color = if (borderWidth != 0.dp) containerColor else Color.Transparent,
                shape = CircleShape,
            )
            .alsoIf(
                enabled,
                Modifier.clickableOnce(onClick = onClick)
            )
    ) {
        Icon(
            painter = painterResource(resource = iconRes),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier
                .alpha(alpha = animatedAlpha)
                .size(size = ICON_SIZE.dp)
        )
    }
}

@Composable
private fun IconButtonWithText(
    text: String,
    iconRes: DrawableResource,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    borderWidth: Dp = 0.dp,
    animatedAlpha: Float = 1f,
    onClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(height = BUTTON_SIZE.dp)
            .clip(shape = RoundedCornerShape(size = 32.dp))
            .alsoIf(
                enabled,
                Modifier.clickableOnce(onClick = onClick)
            )
            .background(
                color = if (borderWidth != 0.dp) Color.Transparent else containerColor,
                shape = CircleShape,
            )
            .border(
                width = borderWidth,
                color = if (borderWidth != 0.dp) containerColor else Color.Transparent,
                shape = CircleShape,
            )
            .padding(horizontal = 16.dp)
    ) {
        Icon(
            painter = painterResource(resource = iconRes),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier
                .alpha(alpha = animatedAlpha)
                .size(size = ICON_SIZE.dp)
        )

        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .alpha(alpha = animatedAlpha)
                .padding(start = 8.dp)
        )
    }
}

private const val ALPHA_ENABLED = 1f
private const val ALPHA_DISABLED = 0.5f
private const val ALPHA_ANIM_DURATION = 150
private const val BUTTON_SIZE = 44
private const val ICON_SIZE = 22

@Preview
@Composable
private fun TackleIconButtonPreviewLight() {
    TackleScreenPreview {
        TackleIconButtonPreviewContent()
    }
}

@Preview
@Composable
private fun TackleIconButtonPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        TackleIconButtonPreviewContent()
    }
}

@Composable
private fun TackleIconButtonPreviewContent() {
    Column(modifier = Modifier.padding(all = 8.dp)) {
        TackleIconButton(
            iconRes = Res.drawable.editor_send,
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        TackleIconButton(
            iconRes = Res.drawable.editor_send,
            borderWidth = 1.dp
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        TackleIconButton(
            iconRes = Res.drawable.editor_language,
            additionalText = "English"
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        TackleIconButton(
            iconRes = Res.drawable.editor_language,
            borderWidth = 1.dp,
            additionalText = "English"
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        TackleIconButton(
            iconRes = Res.drawable.editor_send,
            enabled = false,
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        TackleIconButton(
            iconRes = Res.drawable.editor_language,
            additionalText = "English",
            enabled = false,
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
    }
}
