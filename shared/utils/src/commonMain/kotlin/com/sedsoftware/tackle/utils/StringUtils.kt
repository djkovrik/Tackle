package com.sedsoftware.tackle.utils

import com.mohamedrejeb.ksoup.entities.KsoupEntities
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlOptions
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser

object StringUtils {

    private var parsedString: String = ""

    private val ksoupHtmlHandler: KsoupHtmlHandler =
        KsoupHtmlHandler
            .Builder()
            .onText { text -> parsedString += text }
            .onOpenTag { name: String, attributes: Map<String, String>, isImplied: Boolean ->
                if (name == LINE_BREAK) {
                    parsedString += '\n'
                }
                if (name == PARAGRAPH) {
                    parsedString += '\n'
                }
            }
            .onCloseTag { name: String, isImplied: Boolean ->
                if (name == PARAGRAPH) {
                    parsedString += '\n'
                }
            }
            .build()

    private val ksoupHtmlParser: KsoupHtmlParser =
        KsoupHtmlParser(
            handler = ksoupHtmlHandler,
            options = KsoupHtmlOptions(

            )
        )

    fun decodeHtml(html: String): String {
        parsedString = ""
        ksoupHtmlParser.reset()
        ksoupHtmlParser.write(html)
        ksoupHtmlParser.end()
        val resultingString = parsedString.trim('\n')
        return KsoupEntities.decodeHtml(resultingString)
    }

    private const val LINE_BREAK = "br"
    private const val PARAGRAPH = "p"
}
