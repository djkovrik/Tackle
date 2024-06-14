package com.sedsoftware.tackle.network.api

import com.sedsoftware.tackle.network.response.ApplicationDetails

interface AuthorizedApi {

    /**
     * Confirms that the app’s OAuth2 credentials work.
     * Requires Authorization Bearer header with the access token
     *
     * @see <a href="https://docs.joinmastodon.org/methods/apps/#verify_credentials">Verify your app works</a>
     */
    suspend fun verifyCredentials(): ApplicationDetails
}
