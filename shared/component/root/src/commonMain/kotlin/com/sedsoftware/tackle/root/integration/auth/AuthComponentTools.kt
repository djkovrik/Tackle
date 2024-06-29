package com.sedsoftware.tackle.root.integration.auth

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.domain.api.TacklePlatformTools
import com.sedsoftware.tackle.domain.model.AppClientData

internal class AuthComponentTools(
    private val platformTools: TacklePlatformTools,
) : AuthComponentGateways.Tools {

    override fun getClientData(): AppClientData = platformTools.getClientData()
    override fun openUrl(url: String?) = platformTools.openUrl(url)
    override fun getTextInputEndDelay(): Long = INPUT_ENDED_DELAY

    private companion object {
        const val INPUT_ENDED_DELAY = 2000L
    }
}
