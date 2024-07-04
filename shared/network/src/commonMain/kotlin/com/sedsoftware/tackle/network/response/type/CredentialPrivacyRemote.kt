package com.sedsoftware.tackle.network.response.type

import com.sedsoftware.tackle.network.serializer.CredentialsPrivacyRemoteSerializer
import kotlinx.serialization.Serializable

@Serializable(with = CredentialsPrivacyRemoteSerializer::class)
enum class CredentialPrivacyRemote {
    PUBLIC,
    UNLISTED,
    PRIVATE,
    DIRECT,
    UNKNOWN;
}
