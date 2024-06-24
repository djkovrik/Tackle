package com.sedsoftware.tackle.root.integration.auth

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.network.api.AuthorizedApi
import com.sedsoftware.tackle.network.api.OAuthApi
import com.sedsoftware.tackle.network.api.UnauthorizedApi
import com.sedsoftware.tackle.network.model.Application
import com.sedsoftware.tackle.network.model.Instance
import com.sedsoftware.tackle.utils.model.AppClientData

internal class AuthComponentApi(
    private val unauthorizedApi: UnauthorizedApi,
    private val authorizedApi: AuthorizedApi,
    private val oauthApi: OAuthApi,
) : AuthComponentGateways.Api {

    override suspend fun getServerInfo(url: String): Instance =
        unauthorizedApi.getServerInfo(url)

    override suspend fun createApp(data: AppClientData): Application =
        unauthorizedApi.createApp(data.name, data.uri, data.scopes, data.website)

    override suspend fun startAuthFlow(id: String, secret: String, uri: String, scopes: String): String =
        oauthApi.startAuthFlow(id, secret, uri, scopes)

    override suspend fun verifyCredentials(): Application =
        authorizedApi.verifyCredentials()
}
