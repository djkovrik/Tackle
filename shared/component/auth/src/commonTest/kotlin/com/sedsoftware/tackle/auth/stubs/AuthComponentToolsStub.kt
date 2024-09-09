package com.sedsoftware.tackle.auth.stubs

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.domain.model.AppClientData

class AuthComponentToolsStub : AuthComponentGateways.Tools {

    override fun getClientData(): AppClientData =
        AppClientData(
            name = AuthComponentApiStubResponses.Constants.NAME,
            uri = AuthComponentApiStubResponses.Constants.URI,
            scopes = AuthComponentApiStubResponses.Constants.SCOPES,
            website = AuthComponentApiStubResponses.Constants.WEBSITE
        )

    override fun openUrl(url: String?) {
        // do nothing
    }

    override fun getTextInputEndDelay(): Long = 0L
}
