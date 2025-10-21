package com.sedsoftware.tackle.main.gateway

import com.sedsoftware.tackle.domain.api.TackleSettings
import com.sedsoftware.tackle.main.StatusComponentGateways

internal class StatusComponentSettings(
    private val settings: TackleSettings,
) : StatusComponentGateways.Settings {

    override val ownUserId: String
        get() = settings.ownId
}
