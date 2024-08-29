package com.sedsoftware.tackle.editor.attachments.stubs

import com.sedsoftware.tackle.domain.model.PlatformFileWrapper

internal object PlatformFileStubs {

    val imageNormal = PlatformFileWrapper(
        name = "normal.jpg",
        extension = "jpg",
        path = "",
        mimeType = "image/jpeg",
        size = 12345L,
        sizeLabel = "",
        readBytes = { ByteArray(0) },
    )

    val imageBig = PlatformFileWrapper(
        name = "big.jpg",
        extension = "jpg",
        path = "",
        mimeType = "image/jpeg",
        size = 1234567890L,
        sizeLabel = "",
        readBytes = { ByteArray(0) },
    )

    val imageEmpty = PlatformFileWrapper(
        name = "empty.jpg",
        extension = "jpg",
        path = "",
        mimeType = "image/jpeg",
        size = 0L,
        sizeLabel = "",
        readBytes = { ByteArray(0) },
    )

    val videoNormal = PlatformFileWrapper(
        name = "normal.mp4",
        extension = "mp4",
        path = "",
        mimeType = "video/mp4",
        size = 12345L,
        sizeLabel = "",
        readBytes = { ByteArray(0) },
    )

    val videoBig = PlatformFileWrapper(
        name = "normal.mp4",
        extension = "mp4",
        path = "",
        mimeType = "video/mp4",
        size = 1234567890L,
        sizeLabel = "",
        readBytes = { ByteArray(0) },
    )

    val fileUnsupported = PlatformFileWrapper(
        name = "test.psd",
        extension = "psd",
        path = "",
        mimeType = "image/vnd.adobe.photoshop",
        size = 12345L,
        sizeLabel = "",
        readBytes = { ByteArray(0) },
    )
}
