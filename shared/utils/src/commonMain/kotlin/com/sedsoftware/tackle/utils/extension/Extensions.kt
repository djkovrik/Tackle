package com.sedsoftware.tackle.utils.extension

import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.rx.observer
import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.utils.extension.ExtensionConstants.BASE_SIZE_VALUE_BINARY
import com.sedsoftware.tackle.utils.extension.ExtensionConstants.BASE_SIZE_VALUE_DECIMAL
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt

val Throwable.isUnauthorized
    get() = this is TackleException.RemoteServerException && this.code == ExtensionConstants.HTTP_CODE_UNAUTHORIZED

val PlatformFileWrapper.isAudio: Boolean
    get() = mimeType.startsWith("audio")

val PlatformFileWrapper.isImage: Boolean
    get() = mimeType.startsWith("image")

val PlatformFileWrapper.isVideo: Boolean
    get() = mimeType.startsWith("video")

fun <T : Any> Store<*, T, *>.asValue(): Value<T> =
    object : Value<T>() {

        override val value: T
            get() = state

        override fun subscribe(observer: (T) -> Unit): Cancellation {
            val disposable = states(observer(onNext = observer))

            return Cancellation {
                disposable.dispose()
            }
        }
    }

fun String.trimUrl(): String =
    replace(" ", "")
        .replace("http://", "")
        .replace("https://", "")
        .substringAfterLast('@')

fun <T> unwrap(result: Result<T>, onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
    with(result) {
        getOrNull()?.let { onSuccess(it) }
        exceptionOrNull()?.let { onError(it) }
    }
}

@Suppress("SwallowedException")
fun String.toLocalDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime = try {
    Instant.parse(this).toLocalDateTime(timeZone = timeZone)
} catch (exception: IllegalArgumentException) {
    Instant.parse(ExtensionConstants.FALLBACK_DATE_TIME).toLocalDateTime(timeZone = timeZone)
}

@Suppress("SwallowedException")
fun String.toLocalDate(): LocalDate = try {
    LocalDate.parse(this)
} catch (exception: IllegalArgumentException) {
    LocalDate.parse(ExtensionConstants.FALLBACK_DATE)
}

fun Long?.orZero(): Long = this ?: 0L

fun Int?.orZero(): Int = this ?: 0

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
    var dotAt = 1
    repeat(decimals) { dotAt *= 10 }
    val roundedValue = (this * dotAt).roundToInt()
    return (roundedValue / dotAt) + (roundedValue % dotAt).toFloat() / dotAt
}

private object ExtensionConstants {
    const val HTTP_CODE_UNAUTHORIZED = 401
    const val FALLBACK_DATE_TIME = "1970-01-01T00:00:00.000Z"
    const val FALLBACK_DATE = "1970-01-01"
    const val BASE_SIZE_VALUE_DECIMAL = 1000.0f
    const val BASE_SIZE_VALUE_BINARY = 1024.0f
}
