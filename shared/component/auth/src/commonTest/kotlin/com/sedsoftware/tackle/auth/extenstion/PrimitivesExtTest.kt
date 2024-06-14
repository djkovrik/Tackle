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
    }

    @Test
    fun `isValidUrl returns false for invalid url`() = runTest {
        assertThat("test".isValidUrl()).isEqualTo(false)
    }
}
