package com.sedsoftware.tackle.auth.stubs

import com.sedsoftware.tackle.auth.Responses
import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.auth.Constants
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.AppClientData
import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.utils.test.BaseStub

class AuthComponentApiStub : BaseStub(), AuthComponentGateways.Api {

    var getServerInfoResponse: Instance = Responses.validInstanceDetails
    var createAppResponse: Application = Responses.validApplicationDetails
    var startAuthFlowResponse: String = Constants.TOKEN
    var verifyCredentialsResponse: Account = Responses.validAccountDetails

    override suspend fun getServerInfo(url: String): Instance =
        asResponse(getServerInfoResponse)

    override suspend fun createApp(data: AppClientData): Application =
        asResponse(createAppResponse)

    override suspend fun startAuthFlow(id: String, secret: String, uri: String, scopes: String): String =
        asResponse(startAuthFlowResponse)

    override suspend fun verifyCredentials(): Account =
        asResponse(verifyCredentialsResponse)
}
