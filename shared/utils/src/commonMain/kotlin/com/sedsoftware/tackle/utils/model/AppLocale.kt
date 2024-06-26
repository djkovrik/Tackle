package com.sedsoftware.tackle.utils.model

data class AppLocale(
    val languageName: String,
    val languageCode: String,
) {
    companion object {
        fun empty(): AppLocale = AppLocale("", "")
    }
}
