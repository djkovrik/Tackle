package com.sedsoftware.tackle.utils

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser

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
}
