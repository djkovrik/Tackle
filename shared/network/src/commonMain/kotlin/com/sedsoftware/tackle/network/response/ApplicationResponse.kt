package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class ApplicationResponse(
    @SerialName("name") val name: String,
    @SerialName("website") val website: String? = null,
    @SerialName("client_id") val clientId: String? = null,
    @SerialName("client_secret") val clientSecret: String? = null,
)
