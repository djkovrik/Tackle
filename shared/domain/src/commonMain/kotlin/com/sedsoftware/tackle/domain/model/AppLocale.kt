package com.sedsoftware.tackle.domain.model

data class AppLocale(
    val languageName: String,
    val languageCode: String,
) {
    companion object {
        fun empty(): AppLocale = AppLocale("", "")
    }
}
