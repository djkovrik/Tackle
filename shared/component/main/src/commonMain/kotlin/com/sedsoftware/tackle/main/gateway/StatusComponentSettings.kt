package com.sedsoftware.tackle.main.gateway

import com.sedsoftware.tackle.domain.api.TackleSettings
import com.sedsoftware.tackle.main.StatusComponentGateways

class StatusComponentSettings(
    private val settings: TackleSettings,
) : StatusComponentGateways.Settings {

    override val ownUserId: String
        get() = settings.ownId
}
