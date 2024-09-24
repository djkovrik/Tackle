package com.sedsoftware.tackle.editor.stubs

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.NewStatusBundle
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.ScheduledStatus
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.domain.model.SearchRequestBundle
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.editor.Responses
import com.sedsoftware.tackle.editor.EditorComponentGateways
import com.sedsoftware.tackle.utils.test.BaseStub

class EditorComponentApiStub : BaseStub(), EditorComponentGateways.Api {

    override suspend fun getServerEmojis(): List<CustomEmoji> =
        asResponse(Responses.emojis)

    override suspend fun sendFile(
        file: PlatformFileWrapper,
        onUpload: (Float) -> Unit,
        thumbnail: PlatformFileWrapper?,
        description: String?,
        focus: String?,
    ): MediaAttachment =
        asResponse(Responses.buildAttachmentResponse(null, null))

    override suspend fun search(bundle: SearchRequestBundle): Search =
        asResponse(Responses.searchResponseDefault)

    override suspend fun sendStatus(bundle: NewStatusBundle): Status =
        asResponse(Responses.statusNormal)

    override suspend fun sendStatusScheduled(bundle: NewStatusBundle): ScheduledStatus =
        asResponse(Responses.statusScheduled)

    override suspend fun updateFile(id: String, description: String?, focus: String?): MediaAttachment =
        asResponse(Responses.buildAttachmentResponse(description, focus))

    override suspend fun getFile(id: String): MediaAttachment =
        asResponse(Responses.buildAttachmentResponse(null, null))
}
