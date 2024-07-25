package com.sedsoftware.tackle.editor.stubs

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.utils.test.StubWithException

class EditorTabComponentApiStub : StubWithException(), EditorTabComponentGateways.Api {

    override suspend fun getServerEmojis(): List<CustomEmoji> = listOf(
        CustomEmoji("kek", "", "", true, "")
    )

    override suspend fun sendFile(
        file: PlatformFileWrapper,
        onUpload: (Int) -> Unit,
        thumbnail: PlatformFileWrapper?,
        description: String?,
        focus: String?,
    ): MediaAttachment = MediaAttachment(
        id = "id",
        type = MediaAttachmentType.IMAGE,
        url = "url",
        remoteUrl = "remoteUrl",
        previewUrl = "previewUrl",
        description = "description",
        blurhash = "blur",
        meta = null,
    )
}
