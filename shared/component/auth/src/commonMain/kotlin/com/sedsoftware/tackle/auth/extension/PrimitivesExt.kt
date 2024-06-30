package com.sedsoftware.tackle.auth.extension

import com.sedsoftware.tackle.utils.trimUrl

internal fun String.normalizeUrl(): String = "https://${this.trimUrl()}"

// TODO Needs some better check?
internal fun String.isValidUrl(): Boolean = contains('.')
