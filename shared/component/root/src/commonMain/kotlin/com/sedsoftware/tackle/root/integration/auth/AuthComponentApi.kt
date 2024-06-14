package com.sedsoftware.tackle.root.integration.auth

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.network.api.AuthorizedApi
import com.sedsoftware.tackle.network.api.OAuthApi
import com.sedsoftware.tackle.network.api.UnauthorizedApi
import com.sedsoftware.tackle.network.response.ApplicationDetails
import com.sedsoftware.tackle.network.response.InstanceDetails
import com.sedsoftware.tackle.utils.model.AppClientData

internal class AuthComponentApi(
    private val unauthorizedApi: UnauthorizedApi,
    private val authorizedApi: AuthorizedApi,
    private val oauthApi: OAuthApi,
) : AuthComponentGateways.Api {

    override suspend fun getServerInfo(url: String): InstanceDetails =
        unauthorizedApi.getServerInfo(url)

    override suspend fun createApp(data: AppClientData): ApplicationDetails =
        unauthorizedApi.createApp(data.name, data.uri, data.scopes, data.website)

    override suspend fun startAuthFlow(id: String, secret: String, uri: String, scopes: String): String =
        oauthApi.startAuthFlow(id, secret, uri, scopes)

    override suspend fun verifyCredentials(): ApplicationDetails =
        authorizedApi.verifyCredentials()
}
