package com.sedsoftware.tackle.editor.stubs

import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.Search
import com.sedsoftware.tackle.domain.model.HashTag
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.utils.test.StubWithException

class EditorTabComponentApiStub : StubWithException(), EditorTabComponentGateways.Api {

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

    var searchResponse: Search = searchResponseDefault

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
    ): Search = asResponse(searchResponse)
}
