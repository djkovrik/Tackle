package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.network.api.OAuthApi
import org.publicvalue.multiplatform.oidc.OpenIdConnectClient
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory
import org.publicvalue.multiplatform.oidc.flows.CodeAuthFlow
import org.publicvalue.multiplatform.oidc.types.CodeChallengeMethod
import org.publicvalue.multiplatform.oidc.types.remote.AccessTokenResponse

internal class TackleOAuthApi(
    private val authFlowFactory: CodeAuthFlowFactory,
    private val domainProvider: () -> String,
) : OAuthApi {

    private val domain: String
        get() = domainProvider.invoke()

    override suspend fun startAuthFlow(id: String, secret: String, uri: String, scopes: String): String {
        val client: OpenIdConnectClient = buildOAuthClient(id, secret, uri, scopes)
        val flow: CodeAuthFlow = authFlowFactory.createAuthFlow(client)
        val tokens: AccessTokenResponse = flow.getAccessToken()
        return tokens.access_token
    }

    private fun buildOAuthClient(id: String, secret: String, uri: String, scopes: String): OpenIdConnectClient {
        return OpenIdConnectClient {
            endpoints {
                authorizationEndpoint = authorizationUrl()
                tokenEndpoint = tokenUrl()
            }

            clientId = id
            clientSecret = secret
            scope = scopes
            redirectUri = uri
            codeChallengeMethod = CodeChallengeMethod.off
        }
    }

    private fun authorizationUrl(): String = "$domain/oauth/authorize"
    private fun tokenUrl(): String = "$domain/oauth/token"
}
