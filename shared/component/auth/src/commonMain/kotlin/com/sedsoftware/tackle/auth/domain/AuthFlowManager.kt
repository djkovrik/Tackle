package com.sedsoftware.tackle.auth.domain

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.auth.model.ObtainedCredentials
import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.AppClientData
import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.utils.extension.toNormalizedUrl

internal class AuthFlowManager(
    private val api: AuthComponentGateways.Api,
    private val database: AuthComponentGateways.Database,
    private val settings: AuthComponentGateways.Settings,
    private val tools: AuthComponentGateways.Tools,
) {
    private val clientAppData: AppClientData by lazy {
        tools.getClientData()
    }

    suspend fun getInstanceInfo(url: String): Result<Instance> = runCatching {
        val response = api.getServerInfo(url)
        database.cacheInstanceInfo(response)
        return@runCatching response
    }

    suspend fun verifyCredentials(): Result<Boolean> = runCatching {
        if (settings.domainNormalized.isEmpty() || settings.token.isEmpty()) {
            throw TackleException.MissedRegistrationData
        }

        val response: Account = api.verifyCredentials()

        settings.ownAvatar = response.avatarStatic
        settings.ownUsername = response.acct
        settings.ownId = response.id

        return@runCatching response.acct.isNotEmpty()
    }

    suspend fun createApp(domain: String): Result<ObtainedCredentials> = runCatching {
        settings.domainNormalized = domain.toNormalizedUrl()
        settings.domainShort = domain

        val response: Application = api.createApp(clientAppData)

        settings.clientId = response.clientId
        settings.clientSecret = response.clientSecret

        return@runCatching ObtainedCredentials(
            clientId = response.clientId,
            clientSecret = response.clientSecret,
        )
    }

    suspend fun startAuthFlow(credentials: ObtainedCredentials): Result<Boolean> = runCatching {
        val accessToken = api.startAuthFlow(
            id = credentials.clientId,
            secret = credentials.clientSecret,
            uri = clientAppData.uri,
            scopes = clientAppData.scopes,
        )

        settings.token = accessToken
        return@runCatching accessToken.isNotEmpty()
    }

    fun getTextInputEndDelay(): Long = tools.getTextInputEndDelay()
}
