package com.sedsoftware.tackle.editor.integration.emojis

import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.content.EditorEmojisGateways

internal class EditorEmojisComponentSettings(
    private val settings: EditorTabComponentGateways.Settings,
) : EditorEmojisGateways.Settings {

    override var emojiLastCachedTimestamp: String
        get() = settings.emojiLastCachedTimestamp
        set(value) {
            settings.emojiLastCachedTimestamp = value
        }
}
