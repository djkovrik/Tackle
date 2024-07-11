package com.sedsoftware.tackle.editor.attachments.stubs

import com.sedsoftware.tackle.domain.model.Instance

internal object InstanceConfigStub {
    val config: Instance.Config = Instance.Config(
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
}
