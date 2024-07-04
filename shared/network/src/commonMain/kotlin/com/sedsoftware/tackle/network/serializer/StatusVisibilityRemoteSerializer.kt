package com.sedsoftware.tackle.network.serializer

import com.sedsoftware.tackle.network.response.type.StatusVisibilityRemote
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object StatusVisibilityRemoteSerializer : KSerializer<StatusVisibilityRemote> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = requireNotNull(StatusVisibilityRemote::class.qualifiedName),
        kind = PrimitiveKind.STRING,
    )

    override fun deserialize(decoder: Decoder): StatusVisibilityRemote {
        return StatusVisibilityRemote.entries.firstOrNull { it.name.lowercase() == decoder.decodeString() }
            ?: StatusVisibilityRemote.UNKNOWN
    }

    override fun serialize(encoder: Encoder, value: StatusVisibilityRemote) {
        encoder.encodeString(value.name)
    }
}
