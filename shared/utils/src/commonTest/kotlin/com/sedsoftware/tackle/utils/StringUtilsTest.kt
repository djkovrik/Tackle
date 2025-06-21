package com.sedsoftware.tackle.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class StringUtilsTest {

    @Test
    fun `decodeHtml should return plain text string from html`() = runTest {
        // given
        val content = "\u003cp\u003eStatus with emoji :blobcat: and hashtag \u003ca href=\"https://mastodon.social/tags/protip\" " +
            "class=\"mention hashtag\" rel=\"tag\"\u003e#\u003cspan\u003eprotip\u003c/span\u003e\u003c/a\u003e and mention " +
            "\u003cspan class=\"h-card\" translate=\"no\"\u003e\u003ca href=\"https://mastodon.social/@djkovrik\" class=\"u-url " +
            "mention\"\u003e@\u003cspan\u003edjkovrik\u003c/span\u003e\u003c/a\u003e\u003c/span\u003e ok\u003c/p\u003e"
        val expected = "Status with emoji :blobcat: and hashtag #protip and mention @djkovrik ok"
        // when
        val result = StringUtils.decodeHtml(content)
        // then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decodeHtml should handle line breaks`() = runTest {
        // given
        val content = "<p>Status with emoji :blobcat: and hashtag #protip </ br>and mention @djkovrik ok, another break here<br> and last line.</p>"
        val expected = "Status with emoji :blobcat: and hashtag #protip \nand mention @djkovrik ok, another break here\n and last line."
        // when
        val result = StringUtils.decodeHtml(content)
        // then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decodeHtml should handle p tags`() = runTest {
        // given
        val content = "<p>I will be at the NGI Forum in Brussels next week!</p><p>On Thursday I will participate in the NGI Impact Stories panel " +
                "to explain how is helping Mastodon.</p>"
        val expected = "I will be at the NGI Forum in Brussels next week!\n\nOn Thursday I will participate in the NGI Impact Stories panel " +
                "to explain how is helping Mastodon."
        // when
        val result = StringUtils.decodeHtml(content)
        // then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `decodeHtml should handle special characters`() = runTest {
        // given
        val content = "\u003cp\u003eWe\u0026#39;ve heard your feedback on the Terms of Service updates for mastodon.social and mastodon.online, " +
                " and we\u0026#39;re pausing the implementation date (previously announced to users via email as 1st July 2025)"
        // when
        val result = StringUtils.decodeHtml(content)
        // then
        assertThat(result.isNotEmpty())
    }

}
