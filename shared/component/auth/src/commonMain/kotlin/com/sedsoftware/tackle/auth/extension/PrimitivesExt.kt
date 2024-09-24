package com.sedsoftware.tackle.auth.extension

import com.sedsoftware.tackle.utils.extension.trimUrl

internal object PrimitivesExt {
    val urlRegex = "((http|ftp|https):/{2})?[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-.,@?^=%&amp;:/~+#]*[\\w\\-@?^=%&amp;/~+#])?".toRegex()
}

internal fun String.normalizeUrl(): String {
    val trimmed: String = this.trimUrl()
    return "https://$trimmed"
}

internal fun String.isValidUrl(): Boolean {
    return PrimitivesExt.urlRegex.containsMatchIn(this)
}
