package com.sedsoftware.tackle.root.gateway.editor

import com.sedsoftware.tackle.domain.api.TackleSettings
import com.sedsoftware.tackle.editor.EditorComponentGateways

internal class EditorTabComponentSettings(
    private val settings: TackleSettings,
) : EditorComponentGateways.Settings {


    override val ownAvatar: String
        get() = settings.ownAvatar

    override val ownNickname: String
        get() = settings.ownUsername

    override val domainShort: String
        get() = settings.domainShort

    override var emojiLastCachedTimestamp: String
        get() = settings.emojiLastCachedTimestamp
        set(value) {
            settings.emojiLastCachedTimestamp = value
        }

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
