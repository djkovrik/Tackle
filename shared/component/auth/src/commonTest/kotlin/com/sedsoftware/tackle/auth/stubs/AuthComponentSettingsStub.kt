package com.sedsoftware.tackle.auth.stubs

import com.sedsoftware.tackle.auth.AuthComponentGateways

class AuthComponentSettingsStub : AuthComponentGateways.Settings {
    private var domainStub: String = ""
    private var domainShortStub: String = ""
    private var clientIdStub: String = ""
    private var clientSecretStub: String = ""
    private var tokenStub: String = ""
    private var avatarStub: String = ""
    private var usernameStub: String = ""

    override var domainNormalized: String
        get() = domainStub
        set(value) {
            domainStub = value
        }

    override var domainShort: String
        get() = domainShortStub
        set(value) {
            domainShortStub = value
        }

    override var clientId: String
        get() = clientIdStub
        set(value) {
            clientIdStub = value
        }

    override var clientSecret: String
        get() = clientSecretStub
        set(value) {
            clientSecretStub = value
        }

    override var token: String
        get() = tokenStub
        set(value) {
            tokenStub = value
        }

    override var ownAvatar: String
        get() = avatarStub
        set(value) {
            avatarStub = value
        }

    override var ownUsername: String
        get() = usernameStub
        set(value) {
            usernameStub = value
        }
}
