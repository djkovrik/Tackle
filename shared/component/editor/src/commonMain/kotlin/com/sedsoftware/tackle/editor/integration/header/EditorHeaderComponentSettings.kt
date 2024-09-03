package com.sedsoftware.tackle.editor.integration.header

import com.sedsoftware.tackle.editor.EditorComponentGateways
import com.sedsoftware.tackle.editor.header.EditorHeaderComponentGateways

internal class EditorHeaderComponentSettings(
    private val settings: EditorComponentGateways.Settings,
) : EditorHeaderComponentGateways.Settings {


    override val ownAvatar: String
        get() = settings.ownAvatar

    override val ownNickname: String
        get() = settings.ownNickname

    override val domainShort: String
        get() = settings.domainShort

    override var lastSelectedLanguageName: String
        get() = settings.lastSelectedLanguageName
        set(value) {
            settings.lastSelectedLanguageName = value
        }

    override var lastSelectedLanguageCode: String
        get() = settings.lastSelectedLanguageCode
        set(value) {
            settings.lastSelectedLanguageCode = value
        }
}
