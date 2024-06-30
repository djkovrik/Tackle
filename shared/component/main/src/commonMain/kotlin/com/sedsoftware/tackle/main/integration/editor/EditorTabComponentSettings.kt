package com.sedsoftware.tackle.main.integration.editor

import com.sedsoftware.tackle.domain.api.TackleSettings
import com.sedsoftware.tackle.editor.EditorTabComponentGateways

internal class EditorTabComponentSettings(
    private val settings: TackleSettings,
) : EditorTabComponentGateways.Settings {


    override val ownAvatar: String
        get() = settings.ownAvatar

    override val ownNickname: String
        get() = settings.ownUsername

    override val domain: String
        get() = settings.domain

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
