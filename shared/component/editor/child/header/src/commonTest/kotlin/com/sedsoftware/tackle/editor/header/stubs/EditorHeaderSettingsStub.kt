package com.sedsoftware.tackle.editor.header.stubs

import com.sedsoftware.tackle.editor.header.EditorHeaderComponentGateways

class EditorHeaderSettingsStub : EditorHeaderComponentGateways.Settings {

    var avatar: String = ""
    var nickname: String = ""
    var domain: String = ""
    var lastSelectedName: String = ""
    var lastSelectedCode: String = ""

    override val ownAvatar: String get() = avatar
    override val ownNickname: String get() = nickname
    override val domainShort: String get() = domain

    override var lastSelectedLanguageName: String
        get() = lastSelectedName
        set(value) {
            lastSelectedName = value
        }

    override var lastSelectedLanguageCode: String
        get() = lastSelectedCode
        set(value) {
            lastSelectedCode = value
        }
}
