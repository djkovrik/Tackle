package com.sedsoftware.tackle.editor.integration.header

import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.header.EditorHeaderComponentGateways

internal class EditorHeaderSettings(
    settings: EditorTabComponentGateways.Settings,
) : EditorHeaderComponentGateways.Settings {

    override val ownAvatar: String = settings.ownAvatar
    override val ownNickname: String = settings.ownNickname
    override var lastSelectedLanguageName: String = settings.lastSelectedLanguageName
    override var lastSelectedLanguageCode: String = settings.lastSelectedLanguageCode
}
