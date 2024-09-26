package com.sedsoftware.tackle.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.type.StatusInlineContent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class StringUtilsTest {

    @Test
    fun `decodeHtml should return plain text string from html`() = runTest {
        // given
        val content1 = "\u003cp\u003eStatus with emoji :blobcat: and hashtag \u003ca href=\"https://mastodon.social/tags/protip\" " +
            "class=\"mention hashtag\" rel=\"tag\"\u003e#\u003cspan\u003eprotip\u003c/span\u003e\u003c/a\u003e and mention " +
            "\u003cspan class=\"h-card\" translate=\"no\"\u003e\u003ca href=\"https://mastodon.social/@djkovrik\" class=\"u-url " +
            "mention\"\u003e@\u003cspan\u003edjkovrik\u003c/span\u003e\u003c/a\u003e\u003c/span\u003e ok\u003c/p\u003e"
        val expected1 = "Status with emoji :blobcat: and hashtag #protip and mention @djkovrik ok"
        // when
        val result1 = StringUtils.decodeHtml(content1)
        // then
        assertThat(result1).isEqualTo(expected1)
    }

    @Test
    fun `buildInlineContent should split input string to inline content`() = runTest {
        // given
        val emojis: List<CustomEmoji> = listOf(
            CustomEmoji("lol", "", "", true, ""),
            CustomEmoji("kek", "", "", true, ""),
            CustomEmoji("banana", "", "", true, ""),
        )
        val string1 = "This is some text with :banana: emoji, and broken emoji :lol"
        val string2 = "Text"
        val string3 = ":lol::kek:"
        val string4 = ":lol: :kek:"
        val string5 = "Text :kek:"
        val string6 = ":kek text"

        // when
        val result1 = StringUtils.buildInlineContent(string1, emojis)
        val result2 = StringUtils.buildInlineContent(string2, emojis)
        val result3 = StringUtils.buildInlineContent(string3, emojis)
        val result4 = StringUtils.buildInlineContent(string4, emojis)
        val result5 = StringUtils.buildInlineContent(string5, emojis)
        val result6 = StringUtils.buildInlineContent(string6, emojis)

        // then
        assertThat(result1.size).isEqualTo(3)
        assertThat(result2.size).isEqualTo(1)
        assertThat(result3.size).isEqualTo(2)
        assertThat(result4.size).isEqualTo(3)
        assertThat(result5.size).isEqualTo(2)
        assertThat(result6.size).isEqualTo(1)
    }
}
