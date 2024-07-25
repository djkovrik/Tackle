package com.sedsoftware.tackle.compose.ui.editor.attachment.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.resources.painterResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_retry

@Composable
internal fun AttachedFileUploadFailed(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    indicatorSize: Dp = 48.dp,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size = indicatorSize)
            .clickable(onClick = onClick)
            .clip(shape = CircleShape)
            .background(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
    ) {

        Icon(
            painter = painterResource(resource = Res.drawable.editor_retry),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(all = 10.dp),
        )
    }
}

@Preview
@Composable
private fun AttachedFileIndicatorsPreview() {
    TackleScreenPreview {
        AttachedFileUploadFailed(
            indicatorSize = 64.dp,
        )
    }
}
