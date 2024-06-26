package com.sedsoftware.tackle.domain

import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.Token

interface UnauthorizedApi {

    /**
     * Retrieves basic server instance information.
     *
     * @param url mastodon instance url
     *
     * @see <a href="https://docs.joinmastodon.org/methods/instance/#v2">View server information</a>
     */
    suspend fun getServerInfo(url: String): Instance

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
    suspend fun createApp(client: String, uri: String, scopes: String, website: String): Application

    /**
     * Obtains an access token to be used during API calls that are not public.
     *
     * @param id client id received when app was created
     * @param secret client secret received when app was created
     * @param code code obtained during oauth authorization flow
     * @param uri uri declared when app was created
     * @param scopes list of oauth scopes
     *
     * @see <a href="https://docs.joinmastodon.org/methods/oauth/#token">Obtain a token</a>
     */
    suspend fun obtainToken(id: String, secret: String, code: String, uri: String, scopes: String): Token


    /**
     * Returns custom emojis that are available on the server.
     *
     * @see <a href="https://docs.joinmastodon.org/methods/custom_emojis/#get">View all custom emoji</a>
     */
    suspend fun getServerEmojis(): List<CustomEmoji>
}
