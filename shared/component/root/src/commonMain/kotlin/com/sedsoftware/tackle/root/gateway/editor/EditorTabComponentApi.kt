package com.sedsoftware.tackle.root.gateway.editor

import com.sedsoftware.tackle.domain.api.AuthorizedApi
import com.sedsoftware.tackle.domain.api.UnauthorizedApi
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.NewStatusBundle
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.ScheduledStatus
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.domain.model.SearchRequestBundle
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.editor.EditorComponentGateways

@Suppress("UnusedPrivateProperty")
internal class EditorTabComponentApi(
    private val unauthorizedApi: UnauthorizedApi,
    private val authorizedApi: AuthorizedApi,
) : EditorComponentGateways.Api {

    override suspend fun getServerEmojis(): List<CustomEmoji> = unauthorizedApi.getServerEmojis()

    override suspend fun sendFile(
        file: PlatformFileWrapper,
        onUpload: (Float) -> Unit,
        thumbnail: PlatformFileWrapper?,
        description: String?,
        focus: String?,
    ): MediaAttachment = authorizedApi.sendFile(file, onUpload, thumbnail, description)

    override suspend fun search(bundle: SearchRequestBundle): Search = authorizedApi.search(bundle)

    override suspend fun sendStatus(bundle: NewStatusBundle): Status = authorizedApi.sendStatus(bundle)

    override suspend fun sendStatusScheduled(bundle: NewStatusBundle): ScheduledStatus = authorizedApi.sendStatusScheduled(bundle)

    override suspend fun updateFile(id: String, description: String?, focus: String?): MediaAttachment =
        authorizedApi.updateFile(id, null, description, focus)

    override suspend fun getFile(id: String): MediaAttachment = authorizedApi.getFile(id)
}
