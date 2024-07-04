package com.sedsoftware.tackle.network.response.type

import com.sedsoftware.tackle.network.serializer.PreviewCardTypeRemoteSerializer
import kotlinx.serialization.Serializable

@Serializable(with = PreviewCardTypeRemoteSerializer::class)
internal enum class PreviewCardTypeRemote {
    LINK,
    PHOTO,
    VIDEO,
    RICH,
    UNKNOWN;
}
