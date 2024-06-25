package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RemoteErrorResponse(
    @SerialName("error") val error: String = "",
    @SerialName("error_description ") val errorDescription: String = "",
)
