package com.sedsoftware.tackle.auth.extenstion

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.startsWith
import com.sedsoftware.tackle.auth.extension.isValidUrl
import com.sedsoftware.tackle.auth.extension.normalizeUrl
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class PrimitivesExtTest {

    @Test
    fun `normalizeUrl should return url`() = runTest {
        assertThat("test.ru".normalizeUrl()).startsWith("https")
    }

    @Test
    fun `isValidUrl returns true for valid url`() = runTest {
        assertThat("test.ru".isValidUrl()).isEqualTo(true)
        assertThat("abc.def.com".isValidUrl()).isEqualTo(true)
        assertThat("http://google.com".isValidUrl()).isEqualTo(true)
        assertThat("https://mastodon.social".isValidUrl()).isEqualTo(true)
        assertThat("https://mastodon.social/a/b/c".isValidUrl()).isEqualTo(true)
        assertThat("http://x.ru".isValidUrl()).isEqualTo(true)
    }

    @Test
    fun `isValidUrl returns false for invalid url`() = runTest {
        assertThat("test".isValidUrl()).isEqualTo(false)
        assertThat(" ".isValidUrl()).isEqualTo(false)
        assertThat("http:/".isValidUrl()).isEqualTo(false)
        assertThat("http://".isValidUrl()).isEqualTo(false)
        assertThat("http://x".isValidUrl()).isEqualTo(false)
        assertThat("a b c".isValidUrl()).isEqualTo(false)
        assertThat("test..ru".isValidUrl()).isEqualTo(false)
    }
}
