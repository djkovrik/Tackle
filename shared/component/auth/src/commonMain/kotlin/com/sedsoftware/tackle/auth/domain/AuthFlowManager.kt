package com.sedsoftware.tackle.auth.domain

import com.sedsoftware.tackle.auth.extension.isValidUrl
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
import org.publicvalue.multiplatform.oidc.OpenIdConnectClient
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory
import org.publicvalue.multiplatform.oidc.flows.CodeAuthFlow
import org.publicvalue.multiplatform.oidc.types.CodeChallengeMethod
import org.publicvalue.multiplatform.oidc.types.remote.AccessTokenResponse

internal class AuthFlowManager(
    private val api: AuthFlowApi,
    private val settings: TackleSettings,
    private val platformTools: TacklePlatformTools,
    private val authFlowFactory: CodeAuthFlowFactory,
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

    suspend fun startAuthFlow(credentials: ObtainedCredentials): Result<Boolean> = runCatching {
        val client: OpenIdConnectClient = buildOAuthClient(credentials)
        val flow: CodeAuthFlow = authFlowFactory.createAuthFlow(client)
        val tokens: AccessTokenResponse = flow.getAccessToken()
        settings.token = tokens.access_token
        tokens.access_token.isNotEmpty()
    }

    private fun buildOAuthClient(credentials: ObtainedCredentials): OpenIdConnectClient {
        require(settings.domain.isValidUrl()) { "OAuth client must have valid domain" }

        return OpenIdConnectClient {
            endpoints {
                authorizationEndpoint = authorizationUrl()
                tokenEndpoint = tokenUrl()
            }

            clientId = credentials.clientId
            clientSecret = credentials.clientSecret
            scope = clientAppData.scopes
            redirectUri = clientAppData.uri
            codeChallengeMethod = CodeChallengeMethod.off
        }
    }

    private fun authorizationUrl(): String = "${settings.domain}/oauth/authorize"
    private fun tokenUrl(): String = "${settings.domain}/oauth/token"
}
