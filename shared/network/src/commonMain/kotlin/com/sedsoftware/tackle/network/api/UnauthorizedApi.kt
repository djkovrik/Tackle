package com.sedsoftware.tackle.network.api

import com.sedsoftware.tackle.network.response.ApplicationDetails
import com.sedsoftware.tackle.network.response.InstanceDetails

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
     * @param url mastodon instance url
     * @param client application name
     * @param uris where the user should be redirected after authorization
     * @param scopes space separated list of scopes
     * @param site application website
     *
     * @see <a href="https://docs.joinmastodon.org/methods/apps/#create">Create an application</a>
     */
    suspend fun createApp(url: String, client: String, uris: String, scopes: String, site: String): ApplicationDetails
}
