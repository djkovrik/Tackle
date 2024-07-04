package com.sedsoftware.tackle.root.integration.auth

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.domain.api.TackleSettings

internal class AuthComponentSettings(
    private val settings: TackleSettings,
) : AuthComponentGateways.Settings {

    override var domainNormalized: String
        get() = settings.domainNormalized
        set(value) {
            settings.domainNormalized = value
        }

    override var domainShort: String
        get() = settings.domainShort
        set(value) {
            settings.domainShort = value
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

    override var ownAvatar: String
        get() = settings.ownAvatar
        set(value) {
            settings.ownAvatar = value
        }

    override var ownUsername: String
        get() = settings.ownUsername
        set(value) {
            settings.ownUsername = value
        }
}
