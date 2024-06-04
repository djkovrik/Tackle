package com.sedsoftware.tackle.utils

import com.sedsoftware.tackle.utils.model.AppClientData
import kotlinx.coroutines.flow.StateFlow

interface TacklePlatformTools {
    fun openUrl(url: String?)
    fun openAuthWebsite(domain: String, clientId: String)
    fun handleOAuthResponse(code: String?, error: String?, description: String?)
    fun getOAuthResponseFlow(): StateFlow<Result<String>>
    fun getClientData(): AppClientData
}
