package com.sedsoftware.tackle.main.integration.editor

import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.api.UnauthorizedApi
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.EditorTabComponentGateways

@Suppress("UnusedPrivateProperty")
internal class EditorTabComponentApi(
    private val unauthorizedApi: UnauthorizedApi,
    private val authorizedApi: AuthorizedApi,
) : EditorTabComponentGateways.Api {

    override suspend fun getServerEmojis(): List<CustomEmoji> = unauthorizedApi.getServerEmojis()

    override suspend fun sendFile(
        file: PlatformFileWrapper,
        onUpload: (Int) -> Unit,
        thumbnail: PlatformFileWrapper?,
        description: String?,
        focus: String?,
    ): MediaAttachment = authorizedApi.sendFile(file, onUpload, thumbnail, description)
}
