package com.sedsoftware.tackle.utils.extension

import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper

val Throwable.isUnauthorized
    get() = this is TackleException.RemoteServerException && this.code == ExtensionConstants.HTTP_CODE_UNAUTHORIZED

val PlatformFileWrapper.isAudio: Boolean
    get() = mimeType.startsWith("audio")

val PlatformFileWrapper.isImage: Boolean
    get() = mimeType.startsWith("image")

val PlatformFileWrapper.isVideo: Boolean
    get() = mimeType.startsWith("video")
