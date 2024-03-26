package com.sedsoftware.tackle.auth.domain

import com.sedsoftware.tackle.auth.model.InstanceInfo
import com.sedsoftware.tackle.network.api.UnauthorizedApi
import com.sedsoftware.tackle.network.response.InstanceDetails

internal class InstanceInfoChecker(
    private val api: UnauthorizedApi,
) {

    suspend fun getInstanceInfo(url: String): Result<InstanceInfo> = runCatching {
        val response: InstanceDetails = api.getServerInfo(url)
        InstanceInfo(
            name = response.title,
            description = response.description,
            logoUrl = response.thumbnail.url,
            users = response.usage.users.activePerMonth,
        )
    }
}
