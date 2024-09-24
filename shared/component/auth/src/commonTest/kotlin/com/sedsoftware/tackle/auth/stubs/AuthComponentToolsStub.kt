package com.sedsoftware.tackle.auth.stubs

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.auth.Constants
import com.sedsoftware.tackle.domain.model.AppClientData

class AuthComponentToolsStub : AuthComponentGateways.Tools {

    override fun getClientData(): AppClientData =
        AppClientData(
            name = Constants.NAME,
            uri = Constants.URI,
            scopes = Constants.SCOPES,
            website = Constants.WEBSITE
        )

    override fun openUrl(url: String?) {
        // do nothing
    }

    override fun getTextInputEndDelay(): Long = 0L
}
