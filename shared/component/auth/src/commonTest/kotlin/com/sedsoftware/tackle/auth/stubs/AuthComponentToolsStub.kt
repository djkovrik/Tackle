package com.sedsoftware.tackle.auth.stubs

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.domain.model.AppClientData

class AuthComponentToolsStub : AuthComponentGateways.Tools {

    override fun getClientData(): AppClientData =
        AppClientData(
            name = StubConstants.NAME,
            uri = StubConstants.URI,
            scopes = StubConstants.SCOPES,
            website = StubConstants.WEBSITE
        )

    override fun openUrl(url: String?) {
        // do nothing
    }

    override fun getTextInputEndDelay(): Long = 0L
}
