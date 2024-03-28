package com.sedsoftware.tackle.network

import com.sedsoftware.tackle.network.api.AuthorizedApi
import com.sedsoftware.tackle.network.api.UnauthorizedApi
import com.sedsoftware.tackle.network.internal.TackleAuthorizedApi
import com.sedsoftware.tackle.network.internal.TackleUnauthorizedApi

interface NetworkModule {
    val unauthorized: UnauthorizedApi
    val authorized: AuthorizedApi
}

interface NetworkModuleDependencies

@Suppress("UnusedParameter") // remove on dependencies or remove dependencies
fun NetworkModule(dependencies: NetworkModuleDependencies): NetworkModule {
    return object : NetworkModule {
        override val unauthorized: UnauthorizedApi by lazy {
            TackleUnauthorizedApi()
        }
        override val authorized: AuthorizedApi by lazy {
            TackleAuthorizedApi()
        }
    }
}
