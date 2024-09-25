package com.sedsoftware.tackle.utils.extension

import com.sedsoftware.tackle.utils.extension.ExtensionConstants.BASE_SIZE_VALUE_BINARY
import com.sedsoftware.tackle.utils.extension.ExtensionConstants.BASE_SIZE_VALUE_DECIMAL
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt

fun String.trimUrl(): String =
    replace(" ", "")
        .replace("http://", "")
        .replace("https://", "")
        .substringAfterLast('@')

@Suppress("SwallowedException")
fun String.toLocalDateTimeCustom(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime = try {
    Instant.parse(this).toLocalDateTime(timeZone = timeZone)
} catch (exception: IllegalArgumentException) {
    Instant.parse(ExtensionConstants.FALLBACK_DATE_TIME).toLocalDateTime(timeZone = timeZone)
}

@Suppress("SwallowedException")
fun String.toLocalDateCustom(): LocalDate = try {
    LocalDate.parse(this)
} catch (exception: IllegalArgumentException) {
    LocalDate.parse(ExtensionConstants.FALLBACK_DATE)
}

fun Long?.orZero(): Long = this ?: 0L

fun Int?.orZero(): Int = this ?: 0

fun Float?.orZero(): Float = this ?: 0f

fun Boolean?.orFalse(): Boolean = this ?: false

fun Long?.toHumanReadableSize(useBinary: Boolean = false): String {
    if (this == null) return ""

    val baseValue = if (useBinary) BASE_SIZE_VALUE_BINARY else BASE_SIZE_VALUE_DECIMAL

    val kiloByteAsByte = 1.0f * baseValue
    val megaByteAsByte = 1.0f * baseValue * baseValue

    return when {
        this < kiloByteAsByte -> "${this.toFloat()} b"
        this >= kiloByteAsByte && this < megaByteAsByte -> "${(this / kiloByteAsByte).roundToDecimals(1)} Kb"
        else -> "${(this / megaByteAsByte).roundToDecimals(1)} Mb"
    }
}

fun Float.roundToDecimals(decimals: Int): Float {
    val multiplier = 10
    var dotAt = 1
    repeat(decimals) { dotAt *= multiplier }
    val roundedValue = (this * dotAt).roundToInt()
    return (roundedValue / dotAt) + (roundedValue % dotAt).toFloat() / dotAt
}

fun Pair<Float, Float>.focusToOffset(middleX: Float, middleY: Float): Pair<Float, Float> {
    val offsetX: Float = this.first * middleX
    val offsetY: Float = this.second * middleY * -1f
    return offsetX to offsetY
}

fun Pair<Float, Float>.offsetToFocus(middleX: Float, middleY: Float): Pair<Float, Float> {
    val offsetX: Float = this.first / middleX
    val offsetY: Float = this.second / middleY * -1f
    return offsetX to offsetY
}
