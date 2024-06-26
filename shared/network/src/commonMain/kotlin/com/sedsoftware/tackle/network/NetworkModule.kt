package com.sedsoftware.tackle.network

import com.sedsoftware.tackle.domain.AuthorizedApi
import com.sedsoftware.tackle.domain.OAuthApi
import com.sedsoftware.tackle.domain.UnauthorizedApi
import com.sedsoftware.tackle.network.internal.TackleAuthorizedApi
import com.sedsoftware.tackle.network.internal.TackleOAuthApi
import com.sedsoftware.tackle.network.internal.TackleUnauthorizedApi
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory

interface NetworkModule {
    val unauthorized: UnauthorizedApi
    val authorized: AuthorizedApi
    val oauth: OAuthApi
}

interface NetworkModuleDependencies {
    val authFlowFactory: CodeAuthFlowFactory
    val domainProvider: () -> String
    val tokenProvider: () -> String
}

fun NetworkModule(dependencies: NetworkModuleDependencies): NetworkModule {
    return object : NetworkModule {
        override val unauthorized: UnauthorizedApi by lazy {
            TackleUnauthorizedApi(
                domainProvider = dependencies.domainProvider,
                tokenProvider = dependencies.tokenProvider,
            )
        }

        override val authorized: AuthorizedApi by lazy {
            TackleAuthorizedApi(
                domainProvider = dependencies.domainProvider,
                tokenProvider = dependencies.tokenProvider,
            )
        }

        override val oauth: OAuthApi by lazy {
            TackleOAuthApi(
                authFlowFactory = dependencies.authFlowFactory,
                domainProvider = dependencies.domainProvider,
            )
        }
    }
}
