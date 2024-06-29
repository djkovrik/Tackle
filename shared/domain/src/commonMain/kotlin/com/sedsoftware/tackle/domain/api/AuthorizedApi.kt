package com.sedsoftware.tackle.domain.api

import com.sedsoftware.tackle.domain.model.Account

interface AuthorizedApi {

    /**
     * Confirms that the user token works`.
     * Requires Authorization Bearer header with the access token
     *
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#verify_credentials">Verify account credentials</a>
     */
    suspend fun verifyCredentials(): Account
}
