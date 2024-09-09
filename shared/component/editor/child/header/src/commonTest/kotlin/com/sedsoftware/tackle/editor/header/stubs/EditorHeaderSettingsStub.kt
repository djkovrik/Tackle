package com.sedsoftware.tackle.editor.header.stubs

import com.sedsoftware.tackle.editor.header.EditorHeaderComponentGateways
import com.sedsoftware.tackle.utils.test.BaseStub

class EditorHeaderSettingsStub : BaseStub(), EditorHeaderComponentGateways.Settings {

    var avatar: String = ""
    var nickname: String = ""
    var domain: String = ""
    var lastSelectedName: String = ""
    var lastSelectedCode: String = ""

    override val ownAvatar: String
        get() = asResponse(avatar)

    override val ownNickname: String
        get() = asResponse(nickname)

    override val domainShort: String
        get() = asResponse(domain)

    override var lastSelectedLanguageName: String
        get() = asResponse(lastSelectedName)
        set(value) {
            lastSelectedName = value
            asResponse(Unit)
        }

    override var lastSelectedLanguageCode: String
        get() = asResponse(lastSelectedCode)
        set(value) {
            lastSelectedCode = value
            asResponse(Unit)
        }
}
