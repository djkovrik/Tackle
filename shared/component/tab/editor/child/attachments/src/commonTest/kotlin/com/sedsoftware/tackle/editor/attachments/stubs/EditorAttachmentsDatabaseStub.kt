package com.sedsoftware.tackle.editor.attachments.stubs

import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsGateways
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EditorAttachmentsDatabaseStub : EditorAttachmentsGateways.Database {

    override suspend fun getCachedInstanceInfo(): Flow<Instance> =
        flowOf(
            Instance.empty().copy(
                config = Instance.Config(
                    statuses = Instance.Config.Statuses(
                        maxMediaAttachments = 4,
                    ),
                    mediaAttachments = Instance.Config.MediaAttachments(
                        imageSizeLimit = 123456L,
                        videoSizeLimit = 123456L,
                        supportedMimeTypes = listOf(
                            "video/mp4",
                            "image/jpeg",
                            "image/png",
                        )
                    )
                )
            )
        )
}
