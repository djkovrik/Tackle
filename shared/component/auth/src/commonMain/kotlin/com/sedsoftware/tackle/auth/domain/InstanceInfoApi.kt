package com.sedsoftware.tackle.auth.domain

import com.sedsoftware.tackle.network.response.InstanceDetails

internal interface InstanceInfoApi {
    suspend fun getServerInfo(url: String) : InstanceDetails
}
