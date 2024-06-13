package com.sedsoftware.tackle.network.api

interface OAuthApi {

    /**
     * Starts authorization flow using Kotlin Multiplatform OIDC library.
     *
     * @param id Client id obtained from  createApp call, see [UnauthorizedApi]
     * @param secret Client secret obtained from  createApp call, see [UnauthorizedApi]
     * @param uri redirect uri
     * @param scopes oauth scopes
     *
     * @return access token
     */
    suspend fun startAuthFlow(id: String, secret: String, uri: String, scopes: String): String
}
