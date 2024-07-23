package com.sedsoftware.tackle.compose.ui.editor.emoji.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.resources.painterResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.emoji_sample
import tackle.shared.compose.generated.resources.emoji_sample2

@Composable
internal fun EditorEmoji(
    painter: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier.padding(all = 6.dp),
        )
    }
}

@Preview
@Composable
private fun EditorEmojiPreview() {
    TackleScreenPreview {
        Row {
            EditorEmoji(
                painter = painterResource(resource = Res.drawable.emoji_sample),
                modifier = Modifier.size(size = 64.dp),
            )
            EditorEmoji(
                painter = painterResource(resource = Res.drawable.emoji_sample2),
                modifier = Modifier.size(size = 64.dp),
            )
        }
    }
}
