package com.sedsoftware.tackle.auth.domain

import com.sedsoftware.tackle.network.response.ApplicationDetails
import com.sedsoftware.tackle.network.response.InstanceDetails

internal interface AuthFlowApi {
    suspend fun getServerInfo(url: String): InstanceDetails
    suspend fun verifyCredentials(): ApplicationDetails
}
