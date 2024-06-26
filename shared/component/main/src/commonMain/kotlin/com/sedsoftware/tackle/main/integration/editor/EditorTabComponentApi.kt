package com.sedsoftware.tackle.main.integration.editor

import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.network.api.AuthorizedApi
import com.sedsoftware.tackle.network.api.UnauthorizedApi
import com.sedsoftware.tackle.network.model.CustomEmoji

internal class EditorTabComponentApi(
    private val unauthorizedApi: UnauthorizedApi,
    private val authorizedApi: AuthorizedApi,
) : EditorTabComponentGateways.Api {

    override suspend fun getServerEmojis(): List<CustomEmoji> =
        unauthorizedApi.getServerEmojis()
}
