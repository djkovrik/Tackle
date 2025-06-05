package com.sedsoftware.tackle.auth

import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.AppClientData
import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.Instance

interface AuthComponentGateways {
    interface Api {
        suspend fun getServerInfo(url: String): Instance
        suspend fun createApp(data: AppClientData): Application
        suspend fun startAuthFlow(id: String, secret: String, uri: String, scopes: String): String
        suspend fun verifyCredentials(): Account
    }

    interface Database {
        suspend fun cacheInstanceInfo(info: Instance)
    }

    interface Settings {
        var domainNormalized: String
        var domainShort: String
        var clientId: String
        var clientSecret: String
        var token: String
        var ownAvatar: String
        var ownUsername: String
        var ownId: String
    }

    interface Tools {
        fun getClientData(): AppClientData
        fun openUrl(url: String?)
        fun getTextInputEndDelay(): Long
    }
}
