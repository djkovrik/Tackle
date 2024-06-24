package com.sedsoftware.tackle.network.serializer

import com.sedsoftware.tackle.network.response.type.PreviewCardTypeRemote
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object PreviewCardTypeRemoteSerializer : KSerializer<PreviewCardTypeRemote> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = requireNotNull(PreviewCardTypeRemote::class.qualifiedName),
        kind = PrimitiveKind.STRING,
    )

    override fun deserialize(decoder: Decoder): PreviewCardTypeRemote {
        return PreviewCardTypeRemote.entries.firstOrNull { it.name.lowercase() == decoder.decodeString() } ?: PreviewCardTypeRemote.UNKNOWN
    }

    override fun serialize(encoder: Encoder, value: PreviewCardTypeRemote) {
        encoder.encodeString(value.name)
    }
}
