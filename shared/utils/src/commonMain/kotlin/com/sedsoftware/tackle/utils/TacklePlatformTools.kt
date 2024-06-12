package com.sedsoftware.tackle.utils

import com.sedsoftware.tackle.utils.model.AppClientData

interface TacklePlatformTools {
    fun openUrl(url: String?)
    fun getClientData(): AppClientData
}
