package com.sedsoftware.tackle.root.integration.auth

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.domain.api.TackleDatabase
import com.sedsoftware.tackle.domain.model.Instance

internal class AuthComponentDatabase(
    private val database: TackleDatabase,
) : AuthComponentGateways.Database {

    override suspend fun cacheInstanceInfo(info: Instance) = database.cacheInstanceInfo(info)
}
