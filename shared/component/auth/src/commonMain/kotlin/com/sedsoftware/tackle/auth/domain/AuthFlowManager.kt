package com.sedsoftware.tackle.auth.domain

import com.sedsoftware.tackle.auth.extension.normalizeUrl
import com.sedsoftware.tackle.auth.model.InstanceInfo
import com.sedsoftware.tackle.auth.model.ObtainedCredentials
import com.sedsoftware.tackle.network.response.ApplicationDetails
import com.sedsoftware.tackle.network.response.InstanceDetails
import com.sedsoftware.tackle.settings.api.TackleSettings
import com.sedsoftware.tackle.utils.AppCreationException
import com.sedsoftware.tackle.utils.MissedRegistrationDataException
import com.sedsoftware.tackle.utils.TacklePlatformTools
import com.sedsoftware.tackle.utils.model.AppClientData
import kotlinx.coroutines.flow.StateFlow

internal class AuthFlowManager(
    private val api: AuthFlowApi,
    private val settings: TackleSettings,
    private val platformTools: TacklePlatformTools,
) {
    private val clientAppData: AppClientData by lazy {
        platformTools.getClientData()
    }

    suspend fun getInstanceInfo(url: String): Result<InstanceInfo> = runCatching {
        val response: InstanceDetails = api.getServerInfo(url)
        InstanceInfo(
            domain = response.domain.normalizeUrl(),
            name = response.title,
            description = response.description,
            logoUrl = response.thumbnail.url,
            users = response.usage.users.activePerMonth,
        )
    }

    suspend fun verifyCredentials(): Result<Boolean> = runCatching {
        if (settings.domain.isEmpty() || settings.token.isEmpty()) {
            throw MissedRegistrationDataException
        }

        val response: ApplicationDetails = api.verifyCredentials()
        response.validApiKey.isNotEmpty()
    }

    suspend fun createApp(domain: String): Result<ObtainedCredentials> = runCatching {
        settings.domain = domain

        val response: ApplicationDetails = api.createApp(clientAppData)

        if (response.clientId.isEmpty() || response.clientSecret.isEmpty()) {
            throw AppCreationException
        }

        settings.clientId = response.clientId
        settings.clientSecret = response.clientSecret

        ObtainedCredentials(
            domain = domain,
            clientId = response.clientId,
            clientSecret = response.clientSecret,
            apiKey = response.validApiKey,
        )
    }

    suspend fun refreshAccessToken(code: String): Result<Boolean> = runCatching {
        val response = api.obtainToken(
            id = settings.clientId,
            secret = settings.clientSecret,
            code = code,
            data = clientAppData,
        )

        if (response.accessToken.isNotEmpty()) {
            settings.token = response.accessToken
        }

        response.accessToken.isNotEmpty()
    }

    fun redirectToBrowser(credentials: ObtainedCredentials) {
        platformTools.openAuthWebsite(
            domain = credentials.domain,
            clientId = credentials.clientId,
        )
    }

    fun observeOAuthFlow(): StateFlow<Result<String>> {
        return platformTools.getOAuthResponseFlow()
    }
}
