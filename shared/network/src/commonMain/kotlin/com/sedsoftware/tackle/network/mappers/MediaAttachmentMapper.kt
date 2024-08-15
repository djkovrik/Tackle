package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.MediaAttachmentFocus
import com.sedsoftware.tackle.domain.model.MediaAttachmentInfo
import com.sedsoftware.tackle.domain.model.MediaAttachmentMeta
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.network.response.MediaAttachmentFocusResponse
import com.sedsoftware.tackle.network.response.MediaAttachmentInfoResponse
import com.sedsoftware.tackle.network.response.MediaAttachmentMetaResponse
import com.sedsoftware.tackle.network.response.MediaAttachmentResponse

internal object MediaAttachmentMapper {

    fun map(from: MediaAttachmentResponse): MediaAttachment =
        MediaAttachment(
            id = from.id,
            type = MediaAttachmentType.entries.firstOrNull { it.name.lowercase() == from.type } ?: MediaAttachmentType.UNKNOWN,
            url = from.url,
            previewUrl = from.previewUrl,
            remoteUrl = from.remoteUrl,
            description = from.description,
            blurhash = from.blurhash,
            meta = from.meta?.let { mapMeta(it) },
        )

    private fun mapFocus(from: MediaAttachmentFocusResponse): MediaAttachmentFocus =
        MediaAttachmentFocus(x = from.x, y = from.y)

    private fun mapInfo(from: MediaAttachmentInfoResponse): MediaAttachmentInfo =
        MediaAttachmentInfo(
            width = from.width,
            height = from.height,
            size = from.size,
            aspect = from.aspect,
            frameRate = from.frameRate,
            duration = from.duration,
            bitrate = from.bitrate,
        )

    private fun mapMeta(from: MediaAttachmentMetaResponse): MediaAttachmentMeta =
        MediaAttachmentMeta(
            length = from.length,
            duration = from.duration,
            fps = from.fps,
            size = from.size,
            width = from.width,
            height = from.height,
            aspect = from.aspect,
            audioEncode = from.audioEncode,
            audioBitrate = from.audioBitrate,
            audioChannels = from.audioChannels,
            original = from.original?.let { mapInfo(it) },
            small = from.small?.let { mapInfo(it) },
            focus = from.focus?.let { mapFocus(it) },
        )

}
