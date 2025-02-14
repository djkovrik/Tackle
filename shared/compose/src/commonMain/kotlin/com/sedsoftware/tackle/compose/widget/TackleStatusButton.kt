package com.sedsoftware.tackle.compose.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.clickableOnce
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.status_bookmark
import tackle.shared.compose.generated.resources.status_favorite
import tackle.shared.compose.generated.resources.status_reply

@Composable
internal fun TackleStatusButton(
    iconRes: DrawableResource,
    counter: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(height = BUTTON_SIZE.dp)
            .clip(shape = MaterialTheme.shapes.extraLarge)
            .clickableOnce(onClick = onClick)
            .padding(horizontal = 8.dp)
    ) {
        Icon(
            painter = painterResource(resource = iconRes),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(size = ICON_SIZE.dp)
        )

        if (counter > 0) {
            Text(
                text = "$counter",
                color = color,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

private const val BUTTON_SIZE = 40
private const val ICON_SIZE = 24

@Preview
@Composable
private fun TackleStatusButtonPreviewLight() {
    TackleScreenPreview {
        TackleStatusButtonPreviewContent()
    }
}

@Preview
@Composable
private fun TackleStatusButtonPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        TackleStatusButtonPreviewContent()
    }
}

@Composable
private fun TackleStatusButtonPreviewContent() {
    Row {
        TackleStatusButton(
            iconRes = Res.drawable.status_reply,
            counter = 123,
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        TackleStatusButton(
            iconRes = Res.drawable.status_bookmark,
            counter = 2,
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        TackleStatusButton(
            iconRes = Res.drawable.status_favorite,
            counter = 0,
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
    }
}
