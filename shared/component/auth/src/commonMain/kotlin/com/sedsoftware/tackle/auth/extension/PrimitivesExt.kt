package com.sedsoftware.tackle.auth.extension

internal fun String.trimInput(): String =
    replace(" ", "")
        .replace("http", "")
        .replace("https", "")
        .trim('\\', '/', '.', ':')

internal fun String.normalizeInput(): String {
    val trimmed = trimInput()
    return "https://$trimmed"
}
