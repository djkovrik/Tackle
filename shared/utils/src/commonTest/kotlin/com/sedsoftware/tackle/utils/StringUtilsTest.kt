package com.sedsoftware.tackle.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

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

    @Test
    fun `extractYoutubeId should extract video ids`() = runTest {
        // given
        val url1 = "http://www.youtube.com/watch?v=p72I7gRXpgA"
        val url2 = "http://youtube.com/watch?v=bQVoAWSP7k4"
        val url3 = "http://www.youtube.com/watch?v=bQVoAWSP7k4"
        val url4 = "https://www.youtube.com/watch?v=6WZoMAVCggM&feature=youtu.be"
        val url5 = "http://www.youtube.com/watch?v=6WZoMAVCggM&"
        val url6 = "http://www.youtube.com/watch?v=bQVoAWSP7k4&feature=popular"
        val url7 = "http://www.youtube.com/watch?v=McNqjYiFmyQ&feature=related&bhablah"
        val url8 = "www.youtube.com/watch?v=McNqjYiFmyQ&feature=related&bhablah"
        val url9 = "http://youtu.be/6WZoMAVCggM"
        val url10 = "youtube.com/watch?v=bQVoAWSP7k4"
        val url11 = "http://youtube.com/watch?v=bQVoAWSP7k4"
        val url12 = "http://youtu.be/bQVoAWSP7k4"
        val url13 = "https://nonvalidurl.com"
        // when
        val result1 = StringUtils.extractYoutubeId(url1)
        val result2 = StringUtils.extractYoutubeId(url2)
        val result3 = StringUtils.extractYoutubeId(url3)
        val result4 = StringUtils.extractYoutubeId(url4)
        val result5 = StringUtils.extractYoutubeId(url5)
        val result6 = StringUtils.extractYoutubeId(url6)
        val result7 = StringUtils.extractYoutubeId(url7)
        val result8 = StringUtils.extractYoutubeId(url8)
        val result9 = StringUtils.extractYoutubeId(url9)
        val result10 = StringUtils.extractYoutubeId(url10)
        val result11 = StringUtils.extractYoutubeId(url11)
        val result12 = StringUtils.extractYoutubeId(url12)
        val result13 = StringUtils.extractYoutubeId(url13)
        // then
        assertEquals(result1, "p72I7gRXpgA")
        assertEquals(result2, "bQVoAWSP7k4")
        assertEquals(result3, "bQVoAWSP7k4")
        assertEquals(result4, "6WZoMAVCggM")
        assertEquals(result5, "6WZoMAVCggM")
        assertEquals(result6, "bQVoAWSP7k4")
        assertEquals(result7, "McNqjYiFmyQ")
        assertEquals(result8, "McNqjYiFmyQ")
        assertEquals(result9, "6WZoMAVCggM")
        assertEquals(result10, "bQVoAWSP7k4")
        assertEquals(result11, "bQVoAWSP7k4")
        assertEquals(result12, "bQVoAWSP7k4")
        assertEquals(result13, "")
    }

}
