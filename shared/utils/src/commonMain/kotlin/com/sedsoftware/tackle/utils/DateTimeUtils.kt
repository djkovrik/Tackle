package com.sedsoftware.tackle.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object DateTimeUtils {
    fun getDateTimeFromPickers(millis: Long, hour: Int, minute: Int, timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime {
        val selected: LocalDate = Instant.fromEpochMilliseconds(millis).toLocalDateTime(timeZone = timeZone).date
        return with(selected) { LocalDateTime(year, month, dayOfMonth, hour, minute) }
    }

    fun getDisplayingLabelFromPickers(millis: Long, hour: Int, minute: Int): String {
        if (millis < 0L || hour < 0 || minute < 0) return ""

        val selectedDateTime: LocalDateTime = getDateTimeFromPickers(millis, hour, minute)

        val outputFormat = LocalDateTime.Format {
            dayOfMonth(padding = Padding.ZERO)
            char('.')
            monthNumber(padding = Padding.ZERO)
            char('.')
            year()
            char(' ')
            hour(padding = Padding.ZERO)
            char(':')
            minute(padding = Padding.ZERO)
        }

        return selectedDateTime.format(outputFormat)
    }

    fun getAsDateTimeUTC(fromDate: LocalDateTime, fromTimeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime {
        return fromDate.toInstant(timeZone = fromTimeZone).toLocalDateTime(timeZone = TimeZone.UTC)
    }
}
