package com.sedsoftware.tackle.compose.core

import com.sedsoftware.tackle.domain.TackleException
import org.jetbrains.compose.resources.getString
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.common_error_no_internet
import tackle.shared.compose.generated.resources.common_error_serialization
import tackle.shared.compose.generated.resources.common_error_server
import tackle.shared.compose.generated.resources.common_error_unknown

internal suspend fun exceptionToString(exception: TackleException): String = when (exception) {
    is TackleException.NetworkException -> getString(Res.string.common_error_no_internet)
    is TackleException.RemoteServerException -> "${getString(Res.string.common_error_server)}: ${exception.code}"
    is TackleException.SerializationException -> getString(Res.string.common_error_serialization)
    is TackleException.Unknown -> getString(Res.string.common_error_unknown)
    else -> ""
}
