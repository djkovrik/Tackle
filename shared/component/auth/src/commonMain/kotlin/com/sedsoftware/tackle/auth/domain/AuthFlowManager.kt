package com.sedsoftware.tackle.auth.domain

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.auth.model.ObtainedCredentials
import com.sedsoftware.tackle.network.model.Account
import com.sedsoftware.tackle.network.model.Application
import com.sedsoftware.tackle.network.model.Instance
import com.sedsoftware.tackle.utils.AppCreationException
import com.sedsoftware.tackle.utils.MissedRegistrationDataException
import com.sedsoftware.tackle.utils.model.AppClientData

internal class AuthFlowManager(
    private val api: AuthComponentGateways.Api,
    private val settings: AuthComponentGateways.Settings,
    private val tools: AuthComponentGateways.Tools,
) {
    private val clientAppData: AppClientData by lazy {
        tools.getClientData()
    }

    suspend fun getInstanceInfo(url: String): Result<Instance> = runCatching {
        return@runCatching api.getServerInfo(url)
    }

    suspend fun verifyCredentials(): Result<Boolean> = runCatching {
        if (settings.domain.isEmpty() || settings.token.isEmpty()) {
            throw MissedRegistrationDataException
        }

        val response: Account = api.verifyCredentials()

        if (response.avatarStatic.isNotEmpty()) {
            settings.ownAvatar = response.avatarStatic
        }


        if (response.username.isNotEmpty()) {
            settings.ownUsername = response.username
        }

        return@runCatching response.username.isNotEmpty()
    }

    suspend fun createApp(domain: String): Result<ObtainedCredentials> = runCatching {
        settings.domain = domain

        val response: Application = api.createApp(clientAppData)

        if (response.clientId.isEmpty() || response.clientSecret.isEmpty()) {
            throw AppCreationException
        }

        settings.clientId = response.clientId
        settings.clientSecret = response.clientSecret

        return@runCatching ObtainedCredentials(
            domain = domain,
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
