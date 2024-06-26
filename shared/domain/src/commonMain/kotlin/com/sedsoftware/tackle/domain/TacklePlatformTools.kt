package com.sedsoftware.tackle.domain

import com.sedsoftware.tackle.domain.model.AppClientData

interface TacklePlatformTools {
    fun openUrl(url: String?)
    fun getClientData(): AppClientData
}
