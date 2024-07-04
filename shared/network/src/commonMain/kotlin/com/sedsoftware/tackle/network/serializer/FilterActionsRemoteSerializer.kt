package com.sedsoftware.tackle.network.serializer

import com.sedsoftware.tackle.network.response.type.FilterActionsRemote
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object FilterActionsRemoteSerializer : KSerializer<FilterActionsRemote> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = requireNotNull(FilterActionsRemote::class.qualifiedName),
        kind = PrimitiveKind.STRING,
    )

    override fun deserialize(decoder: Decoder): FilterActionsRemote {
        return FilterActionsRemote.entries.firstOrNull { it.name.lowercase() == decoder.decodeString() } ?: FilterActionsRemote.UNKNOWN
    }

    override fun serialize(encoder: Encoder, value: FilterActionsRemote) {
        encoder.encodeString(value.name)
    }
}
