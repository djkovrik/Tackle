package com.sedsoftware.tackle.network.internal

import com.sedsoftware.tackle.network.api.AuthorizedApi
import com.sedsoftware.tackle.network.mappers.AccountMapper
import com.sedsoftware.tackle.network.mappers.ApplicationMapper
import com.sedsoftware.tackle.network.model.Account
import com.sedsoftware.tackle.network.model.Application
import com.sedsoftware.tackle.network.response.AccountResponse
import com.sedsoftware.tackle.network.response.ApplicationResponse
import io.ktor.http.HttpMethod

internal class TackleAuthorizedApi(
    domainProvider: () -> String,
    tokenProvider: () -> String,
) : BaseApi(domainProvider = domainProvider, tokenProvider = tokenProvider), AuthorizedApi {

    override suspend fun verifyCredentials(): Account =
        doRequest<AccountResponse, Account>(
            requestUrl = "$instanceUrl/api/v1/accounts/verify_credentials",
            requestMethod = HttpMethod.Get,
            authenticated = true,
            responseMapper = AccountMapper::map,
        )
}
