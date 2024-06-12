package com.sedsoftware.tackle.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class TokenRequest(
    @SerialName("client_id") val clientId: String,
    @SerialName("client_secret") val clientSecret: String,
    @SerialName("code") val code: String,
    @SerialName("redirect_uri") val redirectUri: String,
    @SerialName("grant_type") val grantType: String,
    @SerialName("scope") val scopes: String,
)
