package com.sedsoftware.tackle.domain.api

import com.sedsoftware.tackle.domain.model.AppClientData
import com.sedsoftware.tackle.domain.model.AppLocale

interface TacklePlatformTools {
    fun openUrl(url: String?)
    fun getClientData(): AppClientData
    fun getCurrentLocale(): AppLocale
    fun getAvailableLocales(): List<AppLocale>
}
