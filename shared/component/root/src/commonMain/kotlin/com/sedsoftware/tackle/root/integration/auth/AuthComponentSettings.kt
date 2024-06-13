package com.sedsoftware.tackle.root.integration.auth

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.settings.api.TackleSettings

internal class AuthComponentSettings(
    private val settings: TackleSettings,
) : AuthComponentGateways.Settings {

    override var domain: String
        get() = settings.domain
        set(value) {
            settings.domain = value
        }

    override var clientId: String
        get() = settings.clientId
        set(value) {
            settings.clientId = value
        }

    override var clientSecret: String
        get() = settings.clientSecret
        set(value) {
            settings.clientSecret = value
        }

    override var token: String
        get() = settings.token
        set(value) {
            settings.token = value
        }
}
