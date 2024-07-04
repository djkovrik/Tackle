package com.sedsoftware.tackle.network.response.type

import com.sedsoftware.tackle.network.serializer.StatusVisibilityRemoteSerializer
import kotlinx.serialization.Serializable

@Serializable(with = StatusVisibilityRemoteSerializer::class)
enum class StatusVisibilityRemote {
    PUBLIC,
    UNLISTED,
    PRIVATE,
    DIRECT,
    UNKNOWN;
}
