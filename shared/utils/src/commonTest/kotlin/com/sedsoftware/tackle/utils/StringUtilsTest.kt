package com.sedsoftware.tackle.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
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
}
