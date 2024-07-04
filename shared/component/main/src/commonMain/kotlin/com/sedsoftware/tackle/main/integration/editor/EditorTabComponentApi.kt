package com.sedsoftware.tackle.main.integration.editor

import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.api.UnauthorizedApi
import com.sedsoftware.tackle.domain.model.CustomEmoji

@Suppress("UnusedPrivateProperty")
internal class EditorTabComponentApi(
    private val unauthorizedApi: UnauthorizedApi,
    private val authorizedApi: AuthorizedApi,
) : EditorTabComponentGateways.Api {

    override suspend fun getServerEmojis(): List<CustomEmoji> = unauthorizedApi.getServerEmojis()
}
