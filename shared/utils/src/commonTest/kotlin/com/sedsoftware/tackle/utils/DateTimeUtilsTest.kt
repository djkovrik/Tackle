package com.sedsoftware.tackle.utils

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.test.Test

class DateTimeUtilsTest {

    @Test
    fun `getDateTimeFromDatePickers should create date time`() = runTest {
        // given
        val millis = 1724976000000L
        val hour = 12
        val minute = 34
        // when
        val date = DateTimeUtils.getDateTimeFromPickers(millis, hour, minute)
        // then
        assertThat(date.hour).isEqualTo(hour)
        assertThat(date.minute).isEqualTo(minute)
        assertThat(date.year).isEqualTo(2024)
        assertThat(date.monthNumber).isEqualTo(8)
        assertThat(date.dayOfMonth).isEqualTo(30)
    }

    @Test
    fun `getDisplayingLabelFromPickers should format date string`() = runTest {
        // given
        val millis = 1724976000000L
        // when
        val label1 = DateTimeUtils.getDisplayingLabelFromPickers(millis, 1, 2)
        val label2 = DateTimeUtils.getDisplayingLabelFromPickers(millis, 12, 34)
        // then
        assertThat(label1).isEqualTo("30.08.2024 01:02")
        assertThat(label2).isEqualTo("30.08.2024 12:34")
    }

    @Test
    fun `getDisplayingLabelFromPickers returns empty string when date not set`() = runTest {
        // given
        // when
        val label = DateTimeUtils.getDisplayingLabelFromPickers(-1L, -1, -1)
        // then
        assertThat(label).isEmpty()
    }

    @Test
    fun `getAsDateTimeUTC should convert date to UTC timezone`() = runTest {
        // given
        val millis = 1724976000000L
        val hour = 12
        val minute = 34
        val sourceDate = DateTimeUtils.getDateTimeFromPickers(millis, hour, minute, TimeZone.of("GMT+3"))
        val expectedDate = LocalDateTime(2024, 8, 30, 9, 34)
        // when
        val resultingDate = DateTimeUtils.getAsDateTimeUTC(fromDate = sourceDate, fromTimeZone = TimeZone.of("GMT+3"))
        // then
        assertThat(resultingDate).isEqualTo(expectedDate)
    }
}
