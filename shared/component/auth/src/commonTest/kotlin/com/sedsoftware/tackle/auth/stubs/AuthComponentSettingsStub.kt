package com.sedsoftware.tackle.auth.stubs

import com.sedsoftware.tackle.auth.AuthComponentGateways

class AuthComponentSettingsStub : AuthComponentGateways.Settings {
    private var domainStub: String = ""
    private var clientIdStub: String = ""
    private var clientSecretStub: String = ""
    private var tokenStub: String = ""

    override var domain: String
        get() = domainStub
        set(value) {
            domainStub = value
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
}
