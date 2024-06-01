package com.sedsoftware.tackle.auth.domain

import com.sedsoftware.tackle.auth.model.InstanceInfo
import com.sedsoftware.tackle.network.response.ApplicationDetails
import com.sedsoftware.tackle.network.response.InstanceDetails
import com.sedsoftware.tackle.settings.api.TackleSettings
import com.sedsoftware.tackle.utils.MissedRegistrationDataException

internal class AuthFlowManager(
    private val api: AuthFlowApi,
    private val settings: TackleSettings,
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

    suspend fun verifyCredentials(): Result<Boolean> = runCatching {
        if (settings.domain.isEmpty() || settings.token.isEmpty()) {
            throw MissedRegistrationDataException
        }

        val response: ApplicationDetails = api.verifyCredentials()
        !response.name.isNullOrEmpty()
    }
}
