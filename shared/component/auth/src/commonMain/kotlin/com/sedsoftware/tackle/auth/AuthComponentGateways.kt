package com.sedsoftware.tackle.auth

import com.sedsoftware.tackle.network.model.Application
import com.sedsoftware.tackle.network.model.Instance
import com.sedsoftware.tackle.utils.model.AppClientData

interface AuthComponentGateways {
    interface Api {
        suspend fun getServerInfo(url: String): Instance
        suspend fun createApp(data: AppClientData): Application
        suspend fun startAuthFlow(id: String, secret: String, uri: String, scopes: String): String
        suspend fun verifyCredentials(): Application
    }

    interface Settings {
        var domain: String
        var clientId: String
        var clientSecret: String
        var token: String
    }

    interface Tools {
        fun getClientData(): AppClientData
        fun openUrl(url: String?)
        fun getTextInputEndDelay(): Long
    }
}
