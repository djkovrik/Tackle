package com.sedsoftware.tackle.editor.integration.emojis

import com.sedsoftware.tackle.editor.EditorComponentGateways
import com.sedsoftware.tackle.editor.emojis.EditorEmojisGateways

internal class EditorEmojisComponentSettings(
    private val settings: EditorComponentGateways.Settings,
) : EditorEmojisGateways.Settings {

    override var emojiLastCachedTimestamp: String
        get() = settings.emojiLastCachedTimestamp
        set(value) {
            settings.emojiLastCachedTimestamp = value
        }
}
