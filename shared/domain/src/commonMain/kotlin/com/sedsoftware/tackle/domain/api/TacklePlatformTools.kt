package com.sedsoftware.tackle.domain.api

import com.sedsoftware.tackle.domain.model.AppClientData
import com.sedsoftware.tackle.domain.model.AppLocale

interface TacklePlatformTools {

    /**
     * Open web browser with given url
     *
     * @param url target url
     */
    fun openUrl(url: String?)

    /**
     * Get current application client data
     */
    fun getClientData(): AppClientData

    /**
     * Get current system locale
     */
    fun getCurrentLocale(): AppLocale

    /**
     * Get all locales available at the device
     */
    fun getAvailableLocales(): List<AppLocale>
}
