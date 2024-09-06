package com.sedsoftware.tackle.editor.stubs

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.HashTag
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.MediaAttachmentFocus
import com.sedsoftware.tackle.domain.model.MediaAttachmentMeta
import com.sedsoftware.tackle.domain.model.NewStatusBundle
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.ScheduledStatus
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.EditorComponentGateways
import com.sedsoftware.tackle.utils.test.StubWithException

class EditorComponentApiStub : StubWithException(), EditorComponentGateways.Api {

    companion object {
        val searchResponseDefault: Search = Search(
            accounts = listOf(
                AccountStub.accountDetails.copy(id = "1", username = "name1"),
                AccountStub.accountDetails.copy(id = "2", username = "name2"),
                AccountStub.accountDetails.copy(id = "3", username = "name3"),
                AccountStub.accountDetails.copy(id = "4", username = "name4"),
            ),
            hashtags = listOf(
                HashTag("Tag1", "url1", emptyList(), following = false, trendable = false, usable = true, requiresReview = false),
                HashTag("Tag2", "url2", emptyList(), following = false, trendable = false, usable = true, requiresReview = false),
                HashTag("Tag3", "url3", emptyList(), following = false, trendable = false, usable = true, requiresReview = false),
                HashTag("Tag4", "url4", emptyList(), following = false, trendable = false, usable = true, requiresReview = false),
            ),
            statuses = emptyList(),
        )
    }

    override suspend fun getServerEmojis(): List<CustomEmoji> = listOf(
        CustomEmoji("kek", "", "", true, "")
    )

    override suspend fun sendFile(
        file: PlatformFileWrapper,
        onUpload: (Float) -> Unit,
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

    override suspend fun search(
        query: String,
        type: String,
        resolve: Boolean?,
        following: Boolean?,
        accountId: String?,
        excludeUnreviewed: Boolean?,
        minId: String?,
        maxId: String?,
        limit: Int?,
        offset: Int?,
    ): Search = asResponse(searchResponseDefault)

    override suspend fun sendStatus(bundle: NewStatusBundle): Status = asResponse(StatusStub.normal)

    override suspend fun sendStatusScheduled(bundle: NewStatusBundle): ScheduledStatus = asResponse(StatusStub.scheduled)

    override suspend fun updateFile(id: String, description: String?, focus: String?): MediaAttachment = asResponse(
        MediaAttachment(
            id = "id",
            type = MediaAttachmentType.IMAGE,
            url = "url",
            remoteUrl = "remoteUrl",
            previewUrl = "previewUrl",
            description = description.orEmpty(),
            blurhash = "blur",
            meta = MediaAttachmentMeta(
                length = "",
                duration = 0f,
                fps = 0,
                size = "",
                width = 0,
                height = 0,
                aspect = 0f,
                audioEncode = "",
                audioBitrate = "",
                audioChannels = "",
                original = null,
                small = null,
                focus = focus?.let {
                    val x = focus.substringBefore(",").toFloat()
                    val y = focus.substringAfter(",").toFloat()
                    MediaAttachmentFocus(x, y)
                }
            ),
        )
    )
}
