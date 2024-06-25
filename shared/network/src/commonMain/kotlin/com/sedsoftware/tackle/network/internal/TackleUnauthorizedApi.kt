package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.network.api.UnauthorizedApi
import com.sedsoftware.tackle.network.mappers.ApplicationMapper
import com.sedsoftware.tackle.network.mappers.InstanceMapper
import com.sedsoftware.tackle.network.mappers.TokenMapper
import com.sedsoftware.tackle.network.model.Application
import com.sedsoftware.tackle.network.model.Instance
import com.sedsoftware.tackle.network.model.Token
import com.sedsoftware.tackle.network.request.ApplicationRequest
import com.sedsoftware.tackle.network.request.TokenRequest
import com.sedsoftware.tackle.network.response.ApplicationResponse
import com.sedsoftware.tackle.network.response.InstanceResponse
import com.sedsoftware.tackle.network.response.TokenResponse
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

internal class TackleUnauthorizedApi(
    domainProvider: () -> String,
    tokenProvider: () -> String,
) : BaseApi(domainProvider = domainProvider, tokenProvider = tokenProvider), UnauthorizedApi {

    override suspend fun getServerInfo(url: String): Instance =
        doRequest<InstanceResponse, Instance>(
            requestUrl = "$url/api/v2/instance",
            requestMethod = HttpMethod.Get,
            authenticated = false,
            responseMapper = InstanceMapper::map,
        )

    override suspend fun createApp(client: String, uri: String, scopes: String, website: String): Application =
        doRequest<ApplicationResponse, Application>(
            requestUrl = "$instanceUrl/api/v1/apps",
            requestMethod = HttpMethod.Post,
            authenticated = false,
            responseMapper = ApplicationMapper::map,
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                ApplicationRequest(
                    clientName = client,
                    redirectUris = uri,
                    scopes = scopes,
                    website = website
                )
            )
        }


    override suspend fun obtainToken(id: String, secret: String, code: String, uri: String, scopes: String): Token =
        doRequest<TokenResponse, Token>(
            requestUrl = "$instanceUrl/oauth/token",
            requestMethod = HttpMethod.Post,
            authenticated = false,
            responseMapper = TokenMapper::map,
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                TokenRequest(
                    clientId = id,
                    clientSecret = secret,
                    code = code,
                    redirectUri = uri,
                    grantType = Constants.GRANT_TYPE,
                    scopes = scopes,
                )
            )
        }
}
