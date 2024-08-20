package com.sedsoftware.tackle.compose.ui.editor.header.content

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
internal fun ActionBarIcon(
    iconRes: DrawableResource,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: () -> Unit = {},
) {
    val animatedAlpha: Float by animateFloatAsState(
        targetValue = if (enabled) ALPHA_ENABLED else ALPHA_DISABLED,
        animationSpec = tween(
            easing = LinearEasing,
            durationMillis = ALPHA_ANIM_DURATION,
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size = 48.dp)
            .clip(shape = CircleShape)
            .background(color = backgroundColor, shape = CircleShape)
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
                .size(size = 24.dp)
        )
    }
}

private const val ALPHA_ENABLED = 1f
private const val ALPHA_DISABLED = 0.5f
private const val ALPHA_ANIM_DURATION = 150

@Composable
internal fun ActionBarIconWithText(
    text: String,
    iconRes: DrawableResource,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(height = 48.dp)
            .clip(shape = RoundedCornerShape(size = 32.dp))
            .clickableOnce(onClick = onClick)
            .background(color = backgroundColor)
            .padding(horizontal = 16.dp)
    ) {
        Icon(
            painter = painterResource(resource = iconRes),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(size = 24.dp)
        )

        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview
@Composable
private fun ActionBarIconPreview() {
    TackleScreenPreview {
        ActionBarIcon(
            iconRes = Res.drawable.editor_send,
        )
    }
}


@Preview
@Composable
private fun ActionBarIconWithTextPreview() {
    TackleScreenPreview {
        ActionBarIconWithText(
            text = "English",
            iconRes = Res.drawable.editor_language,
        )
    }
}
