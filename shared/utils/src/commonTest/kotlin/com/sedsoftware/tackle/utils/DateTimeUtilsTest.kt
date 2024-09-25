package com.sedsoftware.tackle.utils

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.sedsoftware.tackle.domain.model.type.ShortDateUnit
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

    @Test
    fun `getDateShortLabel should convert date do short label`() = runTest {
        // given
        val timeZone = TimeZone.UTC
        val now = LocalDateTime.parse("2024-09-20T12:34:56.120")
        val fewSecondsAgo1 = LocalDateTime.parse("2024-09-20T12:34:55.120")
        val fewSecondsAgo2 = LocalDateTime.parse("2024-09-20T12:34:50.120")
        val fewSecondsAgo3 = LocalDateTime.parse("2024-09-20T12:34:36.120")
        val seconds1 = LocalDateTime.parse("2024-09-20T12:34:25.120")
        val seconds2 = LocalDateTime.parse("2024-09-20T12:33:58.120")
        val minutes1 = LocalDateTime.parse("2024-09-20T12:33:55.120")
        val minutes2 = LocalDateTime.parse("2024-09-20T12:00:00.120")
        val hours1 = LocalDateTime.parse("2024-09-20T11:32:56.120")
        val hours2 = LocalDateTime.parse("2024-09-20T05:32:56.120")
        val days1 = LocalDateTime.parse("2024-09-19T12:32:56.120")
        val days2 = LocalDateTime.parse("2024-09-18T12:32:56.120")
        val months1 = LocalDateTime.parse("2024-08-20T12:31:56.120")
        val months2 = LocalDateTime.parse("2024-02-20T12:34:56.120")
        val years1 = LocalDateTime.parse("2023-09-20T12:31:56.120")
        val years2 = LocalDateTime.parse("2020-09-20T12:33:56.120")
        // when
        val fewSecondsAgo1Result = DateTimeUtils.getDateShortLabel(fewSecondsAgo1, now, timeZone)
        val fewSecondsAgo2Result = DateTimeUtils.getDateShortLabel(fewSecondsAgo2, now, timeZone)
        val fewSecondsAgo3Result = DateTimeUtils.getDateShortLabel(fewSecondsAgo3, now, timeZone)
        val seconds1Result = DateTimeUtils.getDateShortLabel(seconds1, now, timeZone)
        val seconds2Result = DateTimeUtils.getDateShortLabel(seconds2, now, timeZone)
        val minutes1Result = DateTimeUtils.getDateShortLabel(minutes1, now, timeZone)
        val minutes2Result = DateTimeUtils.getDateShortLabel(minutes2, now, timeZone)
        val hours1Result = DateTimeUtils.getDateShortLabel(hours1, now, timeZone)
        val hours2Result = DateTimeUtils.getDateShortLabel(hours2, now, timeZone)
        val days1Result = DateTimeUtils.getDateShortLabel(days1, now, timeZone)
        val days2Result = DateTimeUtils.getDateShortLabel(days2, now, timeZone)
        val months1Result = DateTimeUtils.getDateShortLabel(months1, now, timeZone)
        val months2Result = DateTimeUtils.getDateShortLabel(months2, now, timeZone)
        val years1Result = DateTimeUtils.getDateShortLabel(years1, now, timeZone)
        val years2Result = DateTimeUtils.getDateShortLabel(years2, now, timeZone)
        // then
        assertThat(fewSecondsAgo1Result).isEqualTo(ShortDateUnit.Now)
        assertThat(fewSecondsAgo2Result).isEqualTo(ShortDateUnit.Now)
        assertThat(fewSecondsAgo3Result).isEqualTo(ShortDateUnit.Now)
        assertThat(seconds1Result).isEqualTo(ShortDateUnit.Seconds(31))
        assertThat(seconds2Result).isEqualTo(ShortDateUnit.Seconds(58))
        assertThat(minutes1Result).isEqualTo(ShortDateUnit.Minutes(1))
        assertThat(minutes2Result).isEqualTo(ShortDateUnit.Minutes(34))
        assertThat(hours1Result).isEqualTo(ShortDateUnit.Hours(1))
        assertThat(hours2Result).isEqualTo(ShortDateUnit.Hours(7))
        assertThat(days1Result).isEqualTo(ShortDateUnit.Days(1))
        assertThat(days2Result).isEqualTo(ShortDateUnit.Days(2))
        assertThat(months1Result).isEqualTo(ShortDateUnit.Months(1))
        assertThat(months2Result).isEqualTo(ShortDateUnit.Months(7))
        assertThat(years1Result).isEqualTo(ShortDateUnit.Years(1))
        assertThat(years2Result).isEqualTo(ShortDateUnit.Years(4))
    }

    @Test
    fun `prettify should format date`() = runTest {
        // given
        val given = LocalDateTime.parse("2023-01-02T23:40:57.120")
        val expected = "02.01.2023 23:40"
        // when
        val result = DateTimeUtils.prettify(given)
        // then
        assertThat(result).isEqualTo(expected)
    }
}
