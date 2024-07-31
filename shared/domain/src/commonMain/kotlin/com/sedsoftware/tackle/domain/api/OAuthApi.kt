package com.sedsoftware.tackle.domain.api

interface OAuthApi {

    /**
     * Starts authorization flow using Kotlin Multiplatform OIDC library.
     *
     * @param id Client id obtained from  createApp call, see [UnauthorizedApi]
     * @param secret Client secret obtained from  createApp call, see [UnauthorizedApi]
     * @param uri Redirect uri
     * @param scopes OAuth scopes
     *
     * @return access token
     */
    suspend fun startAuthFlow(id: String, secret: String, uri: String, scopes: String): String
}
