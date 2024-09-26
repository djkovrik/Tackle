package com.sedsoftware.tackle.utils

object TackleRegex {
    val url: Regex by lazy {
        "((http|ftp|https):/{2})?[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-.,@?^=%&amp;:/~+#]*[\\w\\-@?^=%&amp;/~+#])?".toRegex()
    }

    val hashtagAndMentions: Regex by lazy {
        "((?=[^\\w!])[#@][\\u4e00-\\u9fa5\\w]+)".toRegex()
    }
}
