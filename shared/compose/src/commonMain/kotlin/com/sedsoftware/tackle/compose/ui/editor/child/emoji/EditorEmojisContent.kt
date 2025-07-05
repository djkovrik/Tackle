package com.sedsoftware.tackle.compose.ui.editor.child.emoji

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.github.panpf.sketch.rememberAsyncImagePainter
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.editor.child.emoji.content.EditorEmoji
import com.sedsoftware.tackle.domain.model.CustomEmoji
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_no_category
import tackle.shared.compose.generated.resources.emoji_sample
import tackle.shared.compose.generated.resources.emoji_sample2
import kotlin.random.Random

private val emojiContainerSize = 44.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EditorEmojisContent(
    emojis: Map<String, List<CustomEmoji>>,
    modifier: Modifier = Modifier,
    onClick: (CustomEmoji) -> Unit = {},
    painter: @Composable (String) -> Painter = { rememberAsyncImagePainter(it) },
) {
    BoxWithConstraints(modifier = modifier.background(color = MaterialTheme.colorScheme.background)) {
        val columns = remember { (maxWidth / emojiContainerSize).toInt() }

        LazyColumn {
            emojis.forEach { (category, emojis) ->
                stickyHeader {
                    Text(
                        text = category.ifEmpty {
                            stringResource(resource = Res.string.editor_no_category)
                        },
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                            .padding(all = 8.dp)
                    )
                }

                item {
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        emojis.chunked(columns).forEach { chunk: List<CustomEmoji> ->
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                chunk.forEach { emoji: CustomEmoji ->
                                    EditorEmoji(
                                        painter = painter.invoke(emoji.url),
                                        modifier = Modifier.size(size = emojiContainerSize),
                                        onClick = { onClick.invoke(emoji) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditorEmojisContentPreview() {
    val emojis = buildList {
        repeat(times = 18) { index ->
            add(CustomEmoji("shortcode_$index", "", "", true, "Category 1"))
        }

        repeat(times = 24) { index ->
            add(CustomEmoji("shortcode_$index", "", "", true, "Category 2"))
        }

        repeat(times = 30) { index ->
            add(CustomEmoji("shortcode_$index", "", "", true, ""))
        }
    }
        .groupBy { it.category }

    TackleScreenPreview {
        EditorEmojisContent(
            emojis = emojis,
            painter = { RandomizedPainter() },
        )
    }
}

@Composable
private fun RandomizedPainter(): Painter = if (Random.nextBoolean()) {
    painterResource(Res.drawable.emoji_sample)
} else {
    painterResource(Res.drawable.emoji_sample2)
}

