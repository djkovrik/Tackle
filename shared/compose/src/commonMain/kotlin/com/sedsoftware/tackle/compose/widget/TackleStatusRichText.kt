package com.sedsoftware.tackle.compose.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.sedsoftware.tackle.compose.custom.CustomClickableText
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.PreviewStubs
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.utils.TackleRegex
import com.seiko.imageloader.rememberImagePainter

@Composable
fun TackleStatusRichText(
    status: Status,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    highlightColor: Color = MaterialTheme.colorScheme.secondary,
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
    },
    onClick: (String) -> Unit = {},
) {
    val inlineContent: Map<String, InlineTextContent> = remember {
        buildMap {
            status.emojis.forEach { emoji: CustomEmoji ->
                put(
                    ":${emoji.shortcode}:",
                    InlineTextContent(
                        Placeholder(
                            width = 1.em,
                            height = 1.em,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
                        )
                    ) {
                        inlinedContent.invoke(emoji.staticUrl)
                    }
                )
            }
        }
    }

    val annotatedStringWithEmojis = buildAnnotatedStringWithEmojis(status.contentAsPlainText, textColor)
    val annotatedStringData = buildAnnotatedStringWithList(annotatedStringWithEmojis, highlightColor)


    CustomClickableText(
        text = annotatedStringData.first,
        style = style,
        modifier = modifier,
        inlineContent = inlineContent,
        onClick = { position ->
            val annotatedStringRange = annotatedStringData.second.first { it.start < position && position < it.end }
            if (annotatedStringRange.tag == "link") {
                onClick.invoke(annotatedStringRange.item)
            }
        }
    )
}

@Composable
private fun buildAnnotatedStringWithEmojis(plainText: String, textColor: Color): AnnotatedString {
    val emojis = TackleRegex.emoji

    val annotatedStringList = remember {
        var lastIndex = 0
        val annotatedStringList = mutableStateListOf<AnnotatedString.Range<String>>()

        val results = emojis.findAll(plainText)

        for (match in results) {
            val start = match.range.first
            val end = match.range.last + 1
            val string = plainText.substring(start, end)

            if (start > lastIndex) {
                annotatedStringList.add(
                    AnnotatedString.Range(
                        plainText.substring(lastIndex, start),
                        lastIndex,
                        start,
                        "text"
                    )
                )
            }
            annotatedStringList.add(
                AnnotatedString.Range(string, start, end, "emoji")
            )
            lastIndex = end
        }

        if (lastIndex < plainText.length) {
            annotatedStringList.add(
                AnnotatedString.Range(
                    plainText.substring(lastIndex, plainText.length),
                    lastIndex,
                    plainText.length,
                    "text"
                )
            )
        }

        annotatedStringList
    }

    return buildAnnotatedString {
        annotatedStringList.forEach {
            if (it.tag == "emoji") {
                appendInlineContent(it.item)
            } else {
                withStyle(style = SpanStyle(color = textColor)) { append(it.item) }
            }
        }
    }

}

@Composable
private fun buildAnnotatedStringWithList(
    stringWithEmojis: AnnotatedString,
    highlightColor: Color = MaterialTheme.colorScheme.secondary,
): Pair<AnnotatedString, MutableList<AnnotatedString.Range<String>>> {
    val plainText = stringWithEmojis.text
    val primaryStyle = SpanStyle(color = highlightColor, fontWeight = FontWeight.Bold)

    val hashtags: Regex = TackleRegex.hashtagAndMentions
    val links: Regex = TackleRegex.url

    val annotatedStringList = remember {
        val annotatedStringList = mutableStateListOf<AnnotatedString.Range<String>>()

        val results = hashtags.findAll(plainText) + links.findAll(plainText)

        for (match in results) {
            val start = match.range.first
            val end = match.range.last + 1
            val string = plainText.substring(start, end)

            annotatedStringList.add(
                AnnotatedString.Range(string, start, end, "link")
            )
        }

        annotatedStringList
    }

    val annotatedString = buildAnnotatedString {
        append(stringWithEmojis)

        annotatedStringList.forEach {
            if (it.tag == "link") {
                addStyle(start = it.start, end = it.end, style = primaryStyle)
                addStringAnnotation(tag = it.tag, annotation = it.item, start = it.start, end = it.end)
            }
        }
    }

    return annotatedString to annotatedStringList
}

@Preview
@Composable
private fun TackleStatusRichTextPreviewLight() {
    TackleScreenPreview {
        TackleStatusRichTextContent()
    }
}

@Preview
@Composable
private fun TackleStatusRichTextPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        TackleStatusRichTextContent()
    }
}

@Composable
internal fun TackleStatusRichTextContent() {
    val status = PreviewStubs.statusWithEmbeddedContent
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(all = 16.dp)
    ) {
        Text(
            text = status.contentAsPlainText,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TackleStatusRichText(
            status = status,
            inlinedContent = { Box(modifier = Modifier.fillMaxSize().background(color = Color.Red)) }
        )
    }
}
