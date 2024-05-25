package com.sedsoftware.tackle.network.api

import com.sedsoftware.tackle.network.response.ApplicationDetails
import com.sedsoftware.tackle.network.response.InstanceDetails
import com.sedsoftware.tackle.network.response.TokenDetails

interface UnauthorizedApi {

    /**
     * Retrieves basic server instance information.
     *
     * @param url mastodon instance url
     *
     * @see <a href="https://docs.joinmastodon.org/methods/instance/#v2">View server information</a>
     */
    suspend fun getServerInfo(url: String): InstanceDetails

    /**
     * Creates a new application to obtain OAuth2 credentials.
     *
     * @param client application name
     * @param uri where the user should be redirected after authorization
     * @param scopes space separated list of scopes
     * @param website application website
     *
     * @see <a href="https://docs.joinmastodon.org/methods/apps/#create">Create an application</a>
     */
    suspend fun createApp(client: String, uri: String, scopes: String, website: String): ApplicationDetails

    /**
     * Obtains an access token to be used during API calls that are not public.
     *
     * @param id client id received when app was created
     * @param secret client secret received when app was created
     * @param uri uri declared when app was created
     * @param grantType credentials type
     *
     * @see <a href="https://docs.joinmastodon.org/methods/oauth/#token">Obtain a token</a>
     */
    suspend fun obtainToken(id: String, secret: String, uri: String, grantType: String): TokenDetails
}
