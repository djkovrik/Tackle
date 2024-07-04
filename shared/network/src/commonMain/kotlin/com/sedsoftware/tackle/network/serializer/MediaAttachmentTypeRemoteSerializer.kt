package com.sedsoftware.tackle.network.serializer

import com.sedsoftware.tackle.network.response.type.MediaAttachmentTypeRemote
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object MediaAttachmentTypeRemoteSerializer : KSerializer<MediaAttachmentTypeRemote> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = requireNotNull(MediaAttachmentTypeRemote::class.qualifiedName),
        kind = PrimitiveKind.STRING,
    )

    override fun deserialize(decoder: Decoder): MediaAttachmentTypeRemote {
        return MediaAttachmentTypeRemote.entries.firstOrNull { it.name.lowercase() == decoder.decodeString() }
            ?: MediaAttachmentTypeRemote.UNKNOWN
    }

    override fun serialize(encoder: Encoder, value: MediaAttachmentTypeRemote) {
        encoder.encodeString(value.name)
    }
}
