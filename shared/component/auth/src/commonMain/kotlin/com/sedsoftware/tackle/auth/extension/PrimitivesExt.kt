package com.sedsoftware.tackle.auth.extension

import com.sedsoftware.tackle.utils.TackleRegex
import com.sedsoftware.tackle.utils.extension.trimUrl

internal fun String.normalizeUrl(): String {
    val trimmed: String = this.trimUrl()
    return "https://$trimmed"
}

internal fun String.isValidUrl(): Boolean {
    return TackleRegex.url.containsMatchIn(this)
}
