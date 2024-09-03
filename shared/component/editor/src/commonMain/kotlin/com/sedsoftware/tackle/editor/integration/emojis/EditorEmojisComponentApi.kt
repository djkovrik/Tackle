package com.sedsoftware.tackle.editor.integration.emojis

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.EditorComponentGateways
import com.sedsoftware.tackle.editor.emojis.EditorEmojisGateways

internal class EditorEmojisComponentApi(
    private val api: EditorComponentGateways.Api,
) : EditorEmojisGateways.Api {

    override suspend fun getServerEmojis(): List<CustomEmoji> = api.getServerEmojis()
}
