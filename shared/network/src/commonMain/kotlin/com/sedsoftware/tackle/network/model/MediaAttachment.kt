package com.sedsoftware.tackle.network.model

import com.sedsoftware.tackle.network.model.type.MediaAttachmentType

data class MediaAttachment(
    val id: String,
    val type: MediaAttachmentType,
    val url: String,
    val previewUrl: String,
    val remoteUrl: String,
    val meta: MediaAttachmentMeta,
    val description: String,
    val blurhash: String,
)

data class MediaAttachmentMeta(
    val length: String,
    val duration: Float,
    val fps: Int,
    val size: String,
    val width: Int,
    val height: Int,
    val aspect: Float,
    val audioEncode: String,
    val audioBitrate: String,
    val audioChannels: String,
    val original: MediaAttachmentInfo,
    val small: MediaAttachmentInfo,
    val focus: MediaAttachmentFocus,
)

data class MediaAttachmentInfo(
    val width: Int,
    val height: Int,
    val size: String,
    val aspect: Float,
    val frameRate: String,
    val duration: Float,
    val bitrate: Long,
)

data class MediaAttachmentFocus(
    val x: Float,
    val y: Float,
)
