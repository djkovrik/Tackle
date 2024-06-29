package com.sedsoftware.tackle.root.integration.auth

import com.sedsoftware.tackle.auth.AuthComponentGateways
import com.sedsoftware.tackle.domain.api.TackleSettings

internal class AuthComponentSettings(
    private val settings: TackleSettings,
) : AuthComponentGateways.Settings {

    override var domain: String = settings.domain
    override var clientId: String = settings.clientId
    override var clientSecret: String = settings.clientSecret
    override var token: String = settings.token
    override var ownAvatar: String = settings.ownAvatar
    override var ownUsername: String = settings.ownUsername
}
