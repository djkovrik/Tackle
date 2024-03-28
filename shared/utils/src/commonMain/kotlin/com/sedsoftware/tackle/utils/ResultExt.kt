package com.sedsoftware.tackle.utils

fun <T> unwrap(result: Result<T>, onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
    with(result) {
        getOrNull()?.let { onSuccess(it) }
        exceptionOrNull()?.let { onError(it) }
    }
}
