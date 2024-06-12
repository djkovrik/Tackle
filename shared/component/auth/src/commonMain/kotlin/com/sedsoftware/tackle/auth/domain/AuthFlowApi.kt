package com.sedsoftware.tackle.auth.domain

import com.sedsoftware.tackle.network.response.ApplicationDetails
import com.sedsoftware.tackle.network.response.InstanceDetails
import com.sedsoftware.tackle.utils.model.AppClientData

internal interface AuthFlowApi {
    suspend fun getServerInfo(url: String): InstanceDetails
    suspend fun verifyCredentials(): ApplicationDetails
    suspend fun createApp(data: AppClientData): ApplicationDetails
}
