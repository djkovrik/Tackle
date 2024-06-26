package com.sedsoftware.tackle.main.integration.editor

import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.domain.TackleSettings

internal class EditorTabComponentSettings(
    private val settings: TackleSettings,
) : EditorTabComponentGateways.Settings {

    override val ownAvatar: String
        get() = settings.ownAvatar

    override val ownNickname: String
        get() = settings.ownUsername

    override var emojiLastCachedTimestamp: String
        get() = settings.emojiLastCachedTimestamp
        set(value) {
            settings.emojiLastCachedTimestamp = value
        }
}
