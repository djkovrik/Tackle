package com.sedsoftware.tackle.editor.extension

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.sedsoftware.tackle.domain.model.NewStatusBundle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class NewStatusBundleExtTest {

    @Test
    fun `hasScheduledDate should check if date was scheduled`() = runTest {
        // given
        val bundle1 = NewStatusBundle.Builder()
            .status("Test")
            .build()

        val bundle2 = NewStatusBundle.Builder()
            .status("Test")
            .scheduledAtDate(123L)
            .scheduledAtHour(23)
            .scheduledAtMinute(55)
            .build()
        // when
        val result1 = bundle1.hasScheduledDate
        val result2 = bundle2.hasScheduledDate
        // then
        assertThat(result1).isFalse()
        assertThat(result2).isTrue()
    }
}
