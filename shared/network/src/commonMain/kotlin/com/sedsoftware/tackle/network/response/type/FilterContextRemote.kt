package com.sedsoftware.tackle.network.response.type

import com.sedsoftware.tackle.network.serializer.FilterContextRemoteSerializer
import kotlinx.serialization.Serializable

@Serializable(with = FilterContextRemoteSerializer::class)
internal enum class FilterContextRemote {
    HOME,
    NOTIFICATIONS,
    PUBLIC,
    THREAD,
    ACCOUNT,
    UNKNOWN;
}
