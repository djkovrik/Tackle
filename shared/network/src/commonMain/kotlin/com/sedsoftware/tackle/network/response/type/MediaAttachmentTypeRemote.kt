package com.sedsoftware.tackle.network.response.type

import com.sedsoftware.tackle.network.serializer.MediaAttachmentTypeRemoteSerializer
import kotlinx.serialization.Serializable

@Serializable(with = MediaAttachmentTypeRemoteSerializer::class)
internal enum class MediaAttachmentTypeRemote {
    IMAGE,
    GIF,
    VIDEO,
    AUDIO,
    UNKNOWN;
}
