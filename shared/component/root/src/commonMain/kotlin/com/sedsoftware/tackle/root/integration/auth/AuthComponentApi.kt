package com.sedsoftware.tackle.root.integration.auth

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.domain.AuthorizedApi
import com.sedsoftware.tackle.domain.OAuthApi
import com.sedsoftware.tackle.domain.UnauthorizedApi
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.AppClientData
import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.Instance

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

    override suspend fun verifyCredentials(): Account =
        authorizedApi.verifyCredentials()
}
