package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class SearchResponse(
    @SerialName("accounts") val accounts: List<AccountResponse> = emptyList(),
    @SerialName("statuses") val statuses: List<StatusResponse> = emptyList(),
    @SerialName("hashtags") val hashtags: List<HashTagResponse> = emptyList(),
)
