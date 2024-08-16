package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class MediaAttachmentResponse(
    @SerialName("id") val id: String = "",
    @SerialName("type") val type: String = "",
    @SerialName("url") val url: String = "",
    @SerialName("preview_url") val previewUrl: String = "",
    @SerialName("remote_url") val remoteUrl: String = "",
    @SerialName("meta") val meta: MediaAttachmentMetaResponse? = null,
    @SerialName("description") val description: String = "",
    @SerialName("blurhash") val blurhash: String = "",
)

@Serializable
internal class MediaAttachmentMetaResponse(
    @SerialName("length") val length: String = "",
    @SerialName("duration") val duration: Float = 0f,
    @SerialName("fps") val fps: Int = 0,
    @SerialName("size") val size: String = "",
    @SerialName("width") val width: Int = 0,
    @SerialName("height") val height: Int = 0,
    @SerialName("aspect") val aspect: Float = 0f,
    @SerialName("audio_encode") val audioEncode: String = "",
    @SerialName("audio_bitrate") val audioBitrate: String = "",
    @SerialName("audio_channels") val audioChannels: String = "",
    @SerialName("original") val original: MediaAttachmentInfoResponse? = null,
    @SerialName("small") val small: MediaAttachmentInfoResponse? = null,
    @SerialName("focus") val focus: MediaAttachmentFocusResponse? = null,
)

@Serializable
internal class MediaAttachmentInfoResponse(
    @SerialName("width") val width: Int = 0,
    @SerialName("height") val height: Int = 0,
    @SerialName("size") val size: String = "",
    @SerialName("aspect") val aspect: Float = 0f,
    @SerialName("frame_rate") val frameRate: String = "",
    @SerialName("duration") val duration: Float = 0f,
    @SerialName("bitrate") val bitrate: Long = 0L,
)

@Serializable
internal class MediaAttachmentFocusResponse(
    @SerialName("x") val x: Float = 0f,
    @SerialName("y") val y: Float = 0f,
)
