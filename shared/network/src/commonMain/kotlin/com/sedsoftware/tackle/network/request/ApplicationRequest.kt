package com.sedsoftware.tackle.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class ApplicationRequest(
    @SerialName("client_name") val clientName: String,
    @SerialName("redirect_uris") val redirectUris: String,
    @SerialName("scopes") val scopes: String,
    @SerialName("website") val website: String,
)
