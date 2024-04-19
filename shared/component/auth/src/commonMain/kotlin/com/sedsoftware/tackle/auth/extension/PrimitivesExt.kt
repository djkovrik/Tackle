package com.sedsoftware.tackle.auth.extension

internal fun String.trimForDisplaying(): String =
    replace(" ", "")
        .replace("http://", "")
        .replace("https://", "")
        .substringAfterLast('@')

internal fun String.normalizeForRequest(): String {
    val trimmed = trimForDisplaying()
    return "https://$trimmed"
}

internal fun String.isValidUrl(): Boolean =
    contains('.')
