package com.sedsoftware.tackle.editor.integration.emojis

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.content.EditorEmojisGateways

internal class EditorEmojisComponentApi(
    private val api: EditorTabComponentGateways.Api,
) : EditorEmojisGateways.Api {

    override suspend fun getServerEmojis(): List<CustomEmoji> = api.getServerEmojis()
}
