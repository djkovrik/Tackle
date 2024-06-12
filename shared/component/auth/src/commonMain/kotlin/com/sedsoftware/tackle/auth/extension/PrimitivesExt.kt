package com.sedsoftware.tackle.auth.extension

import com.sedsoftware.tackle.utils.trimUrl

internal fun String.normalizeUrl(): String = "https://${this.trimUrl()}"

internal fun String.isValidUrl(): Boolean = contains('.') // needs some better check?
