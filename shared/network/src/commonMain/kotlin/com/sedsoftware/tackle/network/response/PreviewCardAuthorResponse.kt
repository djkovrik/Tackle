package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class PreviewCardAuthorResponse(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
    @SerialName("account") val account: AccountResponse,
)
