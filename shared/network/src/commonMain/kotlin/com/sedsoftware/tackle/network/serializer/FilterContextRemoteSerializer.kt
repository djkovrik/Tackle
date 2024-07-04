package com.sedsoftware.tackle.network.serializer

import com.sedsoftware.tackle.network.response.type.FilterContextRemote
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object FilterContextRemoteSerializer : KSerializer<FilterContextRemote> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = requireNotNull(FilterContextRemote::class.qualifiedName),
        kind = PrimitiveKind.STRING,
    )

    override fun deserialize(decoder: Decoder): FilterContextRemote {
        return FilterContextRemote.entries.firstOrNull { it.name.lowercase() == decoder.decodeString() } ?: FilterContextRemote.UNKNOWN
    }

    override fun serialize(encoder: Encoder, value: FilterContextRemote) {
        encoder.encodeString(value.name)
    }
}
