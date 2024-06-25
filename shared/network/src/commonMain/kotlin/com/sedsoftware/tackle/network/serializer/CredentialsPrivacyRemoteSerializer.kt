package com.sedsoftware.tackle.network.serializer

import com.sedsoftware.tackle.network.response.type.CredentialPrivacyRemote
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object CredentialsPrivacyRemoteSerializer : KSerializer<CredentialPrivacyRemote> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = requireNotNull(CredentialPrivacyRemote::class.qualifiedName),
        kind = PrimitiveKind.STRING,
    )

    override fun deserialize(decoder: Decoder): CredentialPrivacyRemote {
        return CredentialPrivacyRemote.entries.firstOrNull { it.name.lowercase() == decoder.decodeString() } ?: CredentialPrivacyRemote.UNKNOWN
    }

    override fun serialize(encoder: Encoder, value: CredentialPrivacyRemote) {
        encoder.encodeString(value.name)
    }
}
