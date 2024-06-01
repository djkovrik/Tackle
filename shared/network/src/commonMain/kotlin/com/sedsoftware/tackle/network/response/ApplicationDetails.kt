package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ApplicationDetails(
    @SerialName("name") val name: String? = "",
    @SerialName("website") val website: String? = "",
    @SerialName("client_id") val clientId: String? = "",
    @SerialName("client_secret") val clientSecret: String? = "",
    @SerialName("error") val error: String? = "",
)
