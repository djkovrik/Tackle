package com.sedsoftware.tackle.domain.api

import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.Token
import kotlinx.coroutines.flow.Flow

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
     * @param client Application name
     * @param uri Where the user should be redirected after authorization
     * @param scopes Space separated list of scopes
     * @param website Application website
     *
     * @see <a href="https://docs.joinmastodon.org/methods/apps/#create">Create an application</a>
     */
    suspend fun createApp(client: String, uri: String, scopes: String, website: String): Application

    /**
     * Obtains an access token to be used during API calls that are not public.
     *
     * @param id Client id received when app was created
     * @param secret Client secret received when app was created
     * @param code Code obtained during oauth authorization flow
     * @param uri uri declared when app was created
     * @param scopes List of oauth scopes
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

    /**
     * Direct file download by given url
     *
     * @param url Target file url
     * @param onDone Download completion callback
     *
     * @return Download progress flow
     */
    suspend fun downloadFile(url: String, onDone: suspend (ByteArray) -> Unit): Flow<Float>
}
