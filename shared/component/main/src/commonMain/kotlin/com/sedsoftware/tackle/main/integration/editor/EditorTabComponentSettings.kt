package com.sedsoftware.tackle.main.integration.editor

import com.sedsoftware.tackle.domain.api.TackleSettings
import com.sedsoftware.tackle.editor.EditorTabComponentGateways

internal class EditorTabComponentSettings(
    private val settings: TackleSettings,
) : EditorTabComponentGateways.Settings {

    override val ownAvatar: String = settings.ownAvatar
    override val ownNickname: String = settings.ownUsername
    override val domain: String = settings.domain
    override var emojiLastCachedTimestamp: String = settings.emojiLastCachedTimestamp
    override var lastSelectedLanguageName: String = settings.lastSelectedLanguageName
    override var lastSelectedLanguageCode: String = settings.lastSelectedLanguageCode
}
