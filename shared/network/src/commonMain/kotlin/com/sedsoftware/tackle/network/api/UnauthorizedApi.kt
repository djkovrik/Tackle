package com.sedsoftware.tackle.network.api

import com.sedsoftware.tackle.network.response.InstanceDetails

interface UnauthorizedApi {

    /**
     * Retrieves basic server instance information
     *
     * @see <a href="https://docs.joinmastodon.org/methods/instance/#v2">View server information</a>
     */
    suspend fun getServerInfo(url: String) : InstanceDetails
}
