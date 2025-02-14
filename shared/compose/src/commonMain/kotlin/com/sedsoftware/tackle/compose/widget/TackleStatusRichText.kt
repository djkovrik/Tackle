package com.sedsoftware.tackle.compose.widget

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import com.sedsoftware.tackle.compose.custom.CustomClickableText
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.utils.TackleRegex

@Composable
fun TackleStatusRichText(
    content: String,
    emojis: List<CustomEmoji>,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    highlightColor: Color = MaterialTheme.colorScheme.secondary,
    softWrap: Boolean = true,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    inlinedContent: @Composable (String) -> Unit = {},
    onClick: (String) -> Unit = {},
) {
    val inlineContent: Map<String, InlineTextContent> = remember {
        buildMap {
            emojis.forEach { emoji: CustomEmoji ->
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

    val annotatedStringWithEmojis = buildAnnotatedStringWithEmojis(content, textColor)
    val annotatedStringData = buildAnnotatedStringWithHashTagsAndMentions(annotatedStringWithEmojis, highlightColor)

    CustomClickableText(
        text = annotatedStringData.first,
        style = style,
        modifier = modifier,
        inlineContent = inlineContent,
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
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
private fun buildAnnotatedStringWithHashTagsAndMentions(
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
