package com.sedsoftware.tackle.editor.attachments

import com.sedsoftware.tackle.domain.model.Instance.Config
import com.sedsoftware.tackle.domain.model.Instance.Config.MediaAttachments
import com.sedsoftware.tackle.domain.model.Instance.Config.Statuses
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper

internal object Instances {
    val config: Config = Config(
        statuses = Statuses(
            maxMediaAttachments = 4,
        ),
        mediaAttachments = MediaAttachments(
            imageSizeLimit = 123456L,
            videoSizeLimit = 123456L,
            supportedMimeTypes = listOf(
                "video/mp4",
                "image/jpeg",
                "image/png",
            )
        )
    )


    val imageNormal: PlatformFileWrapper = PlatformFileWrapper(
        name = "normal.jpg",
        extension = "jpg",
        path = "",
        mimeType = "image/jpeg",
        size = 12345L,
        sizeLabel = "",
        readBytes = { ByteArray(0) },
    )

    val imageBig: PlatformFileWrapper = PlatformFileWrapper(
        name = "big.jpg",
        extension = "jpg",
        path = "",
        mimeType = "image/jpeg",
        size = 1234567890L,
        sizeLabel = "",
        readBytes = { ByteArray(0) },
    )

    val imageEmpty: PlatformFileWrapper = PlatformFileWrapper(
        name = "empty.jpg",
        extension = "jpg",
        path = "",
        mimeType = "image/jpeg",
        size = 0L,
        sizeLabel = "",
        readBytes = { ByteArray(0) },
    )

    val videoNormal: PlatformFileWrapper = PlatformFileWrapper(
        name = "normal.mp4",
        extension = "mp4",
        path = "",
        mimeType = "video/mp4",
        size = 12345L,
        sizeLabel = "",
        readBytes = { ByteArray(0) },
    )

    val videoBig: PlatformFileWrapper = PlatformFileWrapper(
        name = "normal.mp4",
        extension = "mp4",
        path = "",
        mimeType = "video/mp4",
        size = 1234567890L,
        sizeLabel = "",
        readBytes = { ByteArray(0) },
    )

    val fileUnsupported: PlatformFileWrapper = PlatformFileWrapper(
        name = "test.psd",
        extension = "psd",
        path = "",
        mimeType = "image/vnd.adobe.photoshop",
        size = 12345L,
        sizeLabel = "",
        readBytes = { ByteArray(0) },
    )
}
