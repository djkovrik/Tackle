package com.sedsoftware.tackle.network.api

import com.sedsoftware.tackle.network.model.Account

interface AuthorizedApi {

    /**
     * Confirms that the user token works`.
     * Requires Authorization Bearer header with the access token
     *
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#verify_credentials">Verify account credentials</a>
     */
    suspend fun verifyCredentials(): Account
}
