package com.sedsoftware.tackle.auth.stubs

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.domain.model.Instance

class AuthComponentDatabaseStub : AuthComponentGateways.Database {

    private var cache: Instance = Instance.empty()

    override suspend fun cacheInstanceInfo(info: Instance) {
        cache = info
    }
}
