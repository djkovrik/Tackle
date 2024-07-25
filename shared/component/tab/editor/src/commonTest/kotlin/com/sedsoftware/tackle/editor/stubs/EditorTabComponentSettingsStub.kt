package com.sedsoftware.tackle.editor.stubs

import com.sedsoftware.tackle.editor.EditorTabComponentGateways

class EditorTabComponentSettingsStub : EditorTabComponentGateways.Settings {
    var lastCachedTimestampStub: String = "2024-07-17"
    var lastSelectedLanguageNameStub: String = "English"
    var lastSelectedLanguageCodeStub: String = "en"

    override val ownAvatar: String = "avatar"
    override val ownNickname: String = "nickname"
    override val domainShort: String = "domainShort"

    override var emojiLastCachedTimestamp: String
        get() = lastCachedTimestampStub
        set(value) {
            lastCachedTimestampStub = value
        }

    override var lastSelectedLanguageName: String
        get() = lastSelectedLanguageNameStub
        set(value) {
            lastSelectedLanguageNameStub = value
        }

    override var lastSelectedLanguageCode: String
        get() = lastSelectedLanguageCodeStub
        set(value) {
            lastSelectedLanguageCodeStub = value
        }
}
