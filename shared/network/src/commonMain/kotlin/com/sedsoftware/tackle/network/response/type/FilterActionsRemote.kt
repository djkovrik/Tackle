package com.sedsoftware.tackle.network.response.type

import com.sedsoftware.tackle.network.serializer.FilterActionsRemoteSerializer
import kotlinx.serialization.Serializable

@Serializable(with = FilterActionsRemoteSerializer::class)
internal enum class FilterActionsRemote {
    WARN,
    HIDE,
    UNKNOWN;
}
