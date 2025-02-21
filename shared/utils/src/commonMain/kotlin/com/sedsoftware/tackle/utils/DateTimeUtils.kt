package com.sedsoftware.tackle.utils

import com.sedsoftware.tackle.domain.model.type.ShortDateUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object DateTimeUtils {
    private val dateTimeFormatter: DateTimeFormat<LocalDateTime> by lazy {
        LocalDateTime.Format {
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
    }

    fun getDateTimeFromPickers(millis: Long, hour: Int, minute: Int, timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime {
        val selected: LocalDate = Instant.fromEpochMilliseconds(millis).toLocalDateTime(timeZone = timeZone).date
        return with(selected) { LocalDateTime(year, month, dayOfMonth, hour, minute) }
    }

    fun getDisplayingLabelFromPickers(millis: Long, hour: Int, minute: Int): String {
        if (millis < 0L || hour < 0 || minute < 0) return ""
        val selectedDateTime: LocalDateTime = getDateTimeFromPickers(millis, hour, minute)
        return selectedDateTime.format(dateTimeFormatter)
    }

    fun getAsDateTimeUTC(fromDate: LocalDateTime, fromTimeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime {
        return fromDate.toInstant(timeZone = fromTimeZone).toLocalDateTime(timeZone = TimeZone.UTC)
    }

    fun getDateShortLabel(date: LocalDateTime, now: LocalDateTime, timeZone: TimeZone = TimeZone.currentSystemDefault()): ShortDateUnit {
        val current: Instant = now.toInstant(timeZone)
        val target: Instant = date.toInstant(timeZone)
        var diffInSeconds: Long = (current - target).inWholeSeconds

        val seconds = diffInSeconds
        diffInSeconds /= MINUTES_PER_HOUR
        val minutes = if (diffInSeconds >= SECONDS_PER_MINUTE) (diffInSeconds % SECONDS_PER_MINUTE) else diffInSeconds
        diffInSeconds /= SECONDS_PER_MINUTE
        val hours = if (diffInSeconds >= HOURS_PER_DAY) (diffInSeconds % HOURS_PER_DAY) else diffInSeconds
        diffInSeconds /= HOURS_PER_DAY
        val days = if (diffInSeconds >= DAYS_PER_MONTH) (diffInSeconds % DAYS_PER_MONTH) else diffInSeconds
        diffInSeconds /= DAYS_PER_MONTH
        val months = if (diffInSeconds >= MONTH_PER_YEAR) (diffInSeconds % MONTH_PER_YEAR) else diffInSeconds
        diffInSeconds /= MONTH_PER_YEAR
        val years = diffInSeconds

        return when {
            years > 0 -> ShortDateUnit.Years(years.toInt())
            months > 0 -> ShortDateUnit.Months(months.toInt())
            days > 0 -> ShortDateUnit.Days(days.toInt())
            hours > 0 -> ShortDateUnit.Hours(hours.toInt())
            minutes > 0 -> ShortDateUnit.Minutes(minutes.toInt())
            seconds > SECONDS_AFTER_NOW -> ShortDateUnit.Seconds(seconds.toInt())
            else -> ShortDateUnit.Now
        }
    }

    fun prettify(from: LocalDateTime): String {
        return from.format(dateTimeFormatter)
    }

    private const val SECONDS_PER_MINUTE = 60
    private const val MINUTES_PER_HOUR = 60
    private const val HOURS_PER_DAY = 24
    private const val DAYS_PER_MONTH = 30
    private const val MONTH_PER_YEAR = 12
    private const val SECONDS_AFTER_NOW = 20
}
