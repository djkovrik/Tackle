package com.sedsoftware.tackle.auth

import com.sedsoftware.tackle.network.response.ApplicationDetails
import com.sedsoftware.tackle.network.response.InstanceDetails
import com.sedsoftware.tackle.utils.model.AppClientData

interface AuthComponentGateways {
    interface Api {
        suspend fun getServerInfo(url: String): InstanceDetails
        suspend fun createApp(data: AppClientData): ApplicationDetails
        suspend fun startAuthFlow(id: String, secret: String, uri: String, scopes: String): String
        suspend fun verifyCredentials(): ApplicationDetails
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
