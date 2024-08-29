package com.sedsoftware.tackle.editor.emojis.stubs

import com.sedsoftware.tackle.editor.emojis.EditorEmojisGateways

class EditorEmojisSettingsStub : EditorEmojisGateways.Settings {

    var lastCachedTimestamp: String = ""

    override var emojiLastCachedTimestamp: String
        get() = lastCachedTimestamp
        set(value) {
            lastCachedTimestamp = value
        }
}
