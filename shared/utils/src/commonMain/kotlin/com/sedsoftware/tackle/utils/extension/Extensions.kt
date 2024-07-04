package com.sedsoftware.tackle.utils.extension

import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.rx.observer
import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.tackle.domain.TackleException
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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

val Throwable.isUnauthorized
    get() = this is TackleException.RemoteServerException && this.code == ExtensionConstants.HTTP_CODE_UNAUTHORIZED

fun Long?.orZero(): Long = this ?: 0L

fun Boolean?.orFalse(): Boolean = this ?: false

private object ExtensionConstants {
    const val HTTP_CODE_UNAUTHORIZED = 401
    const val FALLBACK_DATE_TIME = "1970-01-01T00:00:00.000Z"
    const val FALLBACK_DATE = "1970-01-01"

}
