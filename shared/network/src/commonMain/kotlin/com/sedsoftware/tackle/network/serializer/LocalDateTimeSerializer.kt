package com.sedsoftware.tackle.network.serializer

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object LocalDateTimeSerializer : KSerializer<LocalDateTime> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = requireNotNull(LocalDateTime::class.qualifiedName),
        kind = PrimitiveKind.STRING,
    )

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return try {
            LocalDateTime.parse(decoder.decodeString())
        } catch (exception: IllegalArgumentException) {
            Instant.DISTANT_FUTURE.toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
        }
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }
}
