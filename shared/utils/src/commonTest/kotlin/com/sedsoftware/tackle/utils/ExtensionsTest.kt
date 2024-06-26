package com.sedsoftware.tackle.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinx.datetime.TimeZone
import kotlinx.datetime.TimeZone.Companion
import kotlin.test.Test

class ExtensionsTest {

    private val testDateTime1 = "2024-06-03T12:34:56.000Z"
    private val testDateTime2 = "2024-03-02T00:00:00.000Z"
    private val testDate1 = "2024-06-22"
    private val testDate2 = "1998-01-30"
    private val incorrect = "24/06/22"

    @Test
    fun `toLocalDateTime should parse correct date time string`() {
        // given
        // when
        val dateTime1 = testDateTime1.toLocalDateTime(timeZone = TimeZone.UTC)
        val dateTime2 = testDateTime2.toLocalDateTime(timeZone = TimeZone.UTC)
        // then
        assertThat(dateTime1.year).isEqualTo(2024)
        assertThat(dateTime1.monthNumber).isEqualTo(6)
        assertThat(dateTime1.dayOfMonth).isEqualTo(3)
        assertThat(dateTime1.hour).isEqualTo(12)
        assertThat(dateTime1.minute).isEqualTo(34)
        assertThat(dateTime1.second).isEqualTo(56)

        assertThat(dateTime2.year).isEqualTo(2024)
        assertThat(dateTime2.monthNumber).isEqualTo(3)
        assertThat(dateTime2.dayOfMonth).isEqualTo(2)
        assertThat(dateTime2.hour).isEqualTo(0)
        assertThat(dateTime2.minute).isEqualTo(0)
        assertThat(dateTime2.second).isEqualTo(0)
    }

    @Test
    fun `toLocalDateTime should use fallback for incorrect date time string`() {
        // given
        // when
        val dateTime = incorrect.toLocalDateTime(timeZone = TimeZone.UTC)
        // then
        assertThat(dateTime.year).isEqualTo(1970)
        assertThat(dateTime.monthNumber).isEqualTo(1)
        assertThat(dateTime.dayOfMonth).isEqualTo(1)
        assertThat(dateTime.hour).isEqualTo(0)
        assertThat(dateTime.minute).isEqualTo(0)
        assertThat(dateTime.second).isEqualTo(0)
    }

    @Test
    fun `toLocalDate should parse correct date string`() {
        // given
        // when
        val date1 = testDate1.toLocalDate()
        val date2 = testDate2.toLocalDate()
        // then
        assertThat(date1.year).isEqualTo(2024)
        assertThat(date1.monthNumber).isEqualTo(6)
        assertThat(date1.dayOfMonth).isEqualTo(22)

        assertThat(date2.year).isEqualTo(1998)
        assertThat(date2.monthNumber).isEqualTo(1)
        assertThat(date2.dayOfMonth).isEqualTo(30)
    }

    @Test
    fun `toLocalDate should use fallback date for incorrect date string`() {
        // given
        // when
        val date = incorrect.toLocalDate()
        // then
        assertThat(date.year).isEqualTo(1970)
        assertThat(date.monthNumber).isEqualTo(1)
        assertThat(date.dayOfMonth).isEqualTo(1)
    }
}
