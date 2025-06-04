package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.PreviewStubs
import com.sedsoftware.tackle.compose.widget.TackleStatusRichText
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.utils.extension.isValidUrl
import com.seiko.imageloader.rememberImagePainter
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StatusText(
    status: Status,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    highlightColor: Color = MaterialTheme.colorScheme.secondary,
    onHashTagClick: (String) -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onUrlClick: (String) -> Unit = {},
    inlinedContent: @Composable (String) -> Unit = {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize(),
        ) {
            Image(
                painter = rememberImagePainter(url = it),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = modifier.fillMaxSize(),
            )
        }
    }
) {
    TackleStatusRichText(
        content = status.contentAsPlainText,
        emojis = status.emojis,
        modifier = modifier,
        style = style,
        textColor = textColor,
        highlightColor = highlightColor,
        onClick = { link: String ->
            when {
                link.startsWith("#") -> onHashTagClick.invoke(link)
                link.startsWith("@") -> onMentionClick.invoke(link)
                link.isValidUrl() -> onUrlClick.invoke(link)
            }
        },
        inlinedContent = inlinedContent,
    )
}

@Preview
@Composable
private fun StatusTextPreviewLight() {
    TackleScreenPreview {
        StatusTextPreviewContent()
    }
}

@Preview
@Composable
private fun StatusTextPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        StatusTextPreviewContent()
    }
}

@Composable
private fun StatusTextPreviewContent() {
    val status = PreviewStubs.statusWithEmbeddedContent
    Surface(color = MaterialTheme.colorScheme.background) {
        StatusText(
            status = status,
            inlinedContent = {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Red)
                )
            }
        )
    }
}
