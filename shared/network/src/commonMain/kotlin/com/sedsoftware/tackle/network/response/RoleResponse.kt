package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RoleResponse(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("color") val color: String,
    @SerialName("permissions") val permissions: String,
    @SerialName("highlighted") val highlighted: Boolean,
)
