package com.sedsoftware.tackle.editor.stubs

import com.sedsoftware.tackle.domain.model.Instance

internal object InstanceStub {
    val config: Instance.Config = Instance.Config(
        statuses = Instance.Config.Statuses(
            maxCharacters = 500,
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

    val instance = Instance(
        domain = "domain",
        config = config,
    )
}
