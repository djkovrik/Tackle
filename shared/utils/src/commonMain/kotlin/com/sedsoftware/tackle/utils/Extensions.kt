package com.sedsoftware.tackle.utils

import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.rx.observer
import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isDistantPast
import kotlinx.datetime.toInstant
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

fun String.toLocalDateTime(): LocalDateTime = try {
    LocalDateTime.parse(this)
} catch (exception: IllegalArgumentException) {
    Instant.DISTANT_PAST.toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
}

val LocalDateTime.isValid
    get() = !this.toInstant(timeZone = TimeZone.currentSystemDefault()).isDistantPast

val Throwable.isUnauthorized
    get() = this is RemoteServerException && this.code == HTTP_CODE_UNAUTHORIZED

private const val HTTP_CODE_UNAUTHORIZED = 401
