package com.sedsoftware.tackle.compose.ui.editor.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.seiko.imageloader.rememberImagePainter

@Composable
internal fun InputHintAccount(
    hint: EditorInputHintItem.Account,
    modifier: Modifier = Modifier,
    painter: @Composable (String) -> Painter = { rememberImagePainter(it) },
    onClick: () -> Unit = {},
) {
    InputHintContainer(
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
        ) {
            Box(
                modifier = Modifier
                    .size(size = 24.dp)
                    .clip(shape = CircleShape)
                    .background(color = MaterialTheme.colorScheme.surface)
            ) {
                Image(
                    painter = painter.invoke(hint.avatar),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = CircleShape)
                )
            }

            Text(
                text = hint.username,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
internal fun InputHintEmoji(
    hint: EditorInputHintItem.Emoji,
    painter: @Composable (String) -> Painter = { rememberImagePainter(it) },
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    InputHintContainer(
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .size(size = 24.dp)
                    .clip(shape = CircleShape)
                    .background(color = MaterialTheme.colorScheme.surface)
            ) {
                Image(
                    painter = painter.invoke(hint.url),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = CircleShape)
                )
            }

            Text(
                text = hint.shortcode,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
internal fun InputHintHashTag(
    hint: EditorInputHintItem.HashTag,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    InputHintContainer(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(
            text = "#${hint.text}",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun InputHintContainer(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    Card(
        onClick = onClick,
        modifier = modifier.padding(all = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(size = 32.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(all = 8.dp)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun PreviewHints() {
    TackleScreenPreview {
        Row {
            InputHintAccount(
                hint = EditorInputHintItem.Account("", "testuser", "testuser"),
            )
            InputHintEmoji(
                hint = EditorInputHintItem.Emoji("banana", ""),
            )
            InputHintHashTag(hint = EditorInputHintItem.HashTag("sometag"))
        }
    }
}
