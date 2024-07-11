package com.sedsoftware.tackle.utils

import assertk.assertThat
import assertk.assertions.isNotEmpty
import kotlin.test.Test

class PlatformUtilsTest {

    @Test
    fun `generateUUID should generate id`() {
        // given
        // when
        val id = generateUUID()
        // then
        assertThat(id).isNotEmpty()
    }
}
