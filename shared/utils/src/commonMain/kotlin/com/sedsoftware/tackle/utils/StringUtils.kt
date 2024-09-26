package com.sedsoftware.tackle.utils

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.type.StatusInlineContent

object StringUtils {

    private var parsedString: String = ""

    private val ksoupHtmlHandler: KsoupHtmlHandler =
        KsoupHtmlHandler
            .Builder()
            .onText { text -> parsedString += text }
            .build()

    private val ksoupHtmlParser: KsoupHtmlParser =
        KsoupHtmlParser(
            handler = ksoupHtmlHandler,
        )

    fun decodeHtml(html: String): String {
        parsedString = ""
        ksoupHtmlParser.reset()
        ksoupHtmlParser.write(html)
        ksoupHtmlParser.end()
        return parsedString
    }

    fun buildInlineContent(plainText: String, emojis: List<CustomEmoji>): List<StatusInlineContent> {
        val result: MutableList<StatusInlineContent> = mutableListOf()
        val shortCodes: List<String> = emojis.map { ":${it.shortcode}:" }
        val shortcodesList: MutableList<Pair<Int, Int>> = mutableListOf()
        shortCodes.forEach { shortcode: String ->
            shortcodesList += plainText.indexesOf(shortcode.toRegex())
        }

        val shortcodesMap: Map<Int, Int> = shortcodesList.toMap()
        val emojisMap: Map<String, CustomEmoji> = emojis.associateBy { it.shortcode }

        var pointerLeft = 0
        var pointerRight = 0
        var index = 0
        while (index <= plainText.lastIndex) {
            when {
                shortcodesMap.containsKey(index) -> {
                    if (pointerLeft != pointerRight) {
                        result.add(StatusInlineContent.TextPart(plainText.substring(pointerLeft, pointerRight)))
                    }

                    val firstIndex = index
                    val lastIndex = shortcodesMap[index]!!

                    val shortcode = plainText.substring(firstIndex, lastIndex + 1)
                    result.add(StatusInlineContent.EmojiPart(shortcode, emojisMap[shortcode.trim(':')]!!))

                    val length = lastIndex - firstIndex
                    index += length
                    pointerLeft = index + 1
                    pointerRight = index + 1
                }

                else -> {
                    index += 1
                    pointerRight = index
                }
            }
        }

        if (pointerLeft != pointerRight) {
            result.add(StatusInlineContent.TextPart(plainText.substring(pointerLeft, pointerRight)))
        }

        return result
    }

    private fun String.indexesOf(source: Regex): List<Pair<Int, Int>> =
        source.findAll(this)
            .map { it.range.first to it.range.last }
            .toList()

}
