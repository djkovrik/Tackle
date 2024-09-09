package com.sedsoftware.tackle.auth.stubs

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.AppClientData
import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.utils.test.BaseStub

class AuthComponentApiStub : BaseStub(), AuthComponentGateways.Api {

    var getServerInfoResponse: Instance = AuthComponentApiStubResponses.validInstanceDetails
    var createAppResponse: Application = AuthComponentApiStubResponses.validApplicationDetails
    var startAuthFlowResponse: String = AuthComponentApiStubResponses.Constants.TOKEN
    var verifyCredentialsResponse: Account = AuthComponentApiStubResponses.validAccountDetails

    override suspend fun getServerInfo(url: String): Instance =
        asResponse(getServerInfoResponse)

    override suspend fun createApp(data: AppClientData): Application =
        asResponse(createAppResponse)

    override suspend fun startAuthFlow(id: String, secret: String, uri: String, scopes: String): String =
        asResponse(startAuthFlowResponse)

    override suspend fun verifyCredentials(): Account =
        asResponse(verifyCredentialsResponse)
}
