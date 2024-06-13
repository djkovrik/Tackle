package com.sedsoftware.tackle.root.integration.auth

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.utils.TacklePlatformTools
import com.sedsoftware.tackle.utils.model.AppClientData

internal class AuthComponentTools(
    private val platformTools: TacklePlatformTools,
) : AuthComponentGateways.Tools {

    override fun getClientData(): AppClientData =
        platformTools.getClientData()

    override fun openUrl(url: String?) =
        platformTools.openUrl(url)
}
