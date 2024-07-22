package com.sedsoftware.tackle.compose.ui.editor.emoji

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.ui.editor.emoji.content.EditorEmoji
import com.sedsoftware.tackle.domain.model.CustomEmoji

@Composable
internal fun EditorEmojisComponent(
    emojis: List<CustomEmoji>,
    modifier: Modifier = Modifier,
    onClick: (CustomEmoji) -> Unit = {},
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 32.dp),
        modifier = modifier,
    ) {
        items(emojis) { emoji: CustomEmoji ->
            EditorEmoji(
                emoji = emoji,
                modifier = Modifier
                    .size(size = 32.dp)
                    .padding(all = 1.dp),
                onClick = { onClick.invoke(emoji) }
            )
        }
    }
}
