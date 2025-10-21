package com.sedsoftware.tackle.compose.ui.editor.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.widget.TackleImage
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun InputHintAccount(
    hint: EditorInputHintItem.Account,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: () -> Unit = {},
) {
    InputHintContainer(
        onClick = onClick,
        modifier = modifier,
        containerColor = containerColor,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TackleImage(
                imageUrl = hint.avatar,
                imageParams = null,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size = 24.dp)
                    .clip(shape = CircleShape)
            )

            Text(
                text = hint.username,
                color = contentColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
internal fun InputHintEmoji(
    hint: EditorInputHintItem.Emoji,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: () -> Unit = {},
) {
    InputHintContainer(
        onClick = onClick,
        modifier = modifier,
        containerColor = containerColor,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TackleImage(
                imageUrl = hint.url,
                imageParams = null,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size = 24.dp)
                    .clip(shape = CircleShape)
            )

            Text(
                text = hint.shortcode,
                color = contentColor,
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
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: () -> Unit = {},
) {
    InputHintContainer(
        onClick = onClick,
        modifier = modifier,
        containerColor = containerColor,
    ) {
        Text(
            text = "#${hint.text}",
            color = contentColor,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun InputHintContainer(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    Card(
        onClick = onClick,
        modifier = modifier.padding(all = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(all = 4.dp)
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
