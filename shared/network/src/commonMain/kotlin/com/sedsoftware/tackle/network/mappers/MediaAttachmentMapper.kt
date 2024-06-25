package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.network.model.MediaAttachment
import com.sedsoftware.tackle.network.model.MediaAttachmentFocus
import com.sedsoftware.tackle.network.model.MediaAttachmentInfo
import com.sedsoftware.tackle.network.model.MediaAttachmentMeta
import com.sedsoftware.tackle.network.model.type.MediaAttachmentType
import com.sedsoftware.tackle.network.response.MediaAttachmentFocusResponse
import com.sedsoftware.tackle.network.response.MediaAttachmentInfoResponse
import com.sedsoftware.tackle.network.response.MediaAttachmentMetaResponse
import com.sedsoftware.tackle.network.response.MediaAttachmentResponse
import com.sedsoftware.tackle.network.response.type.MediaAttachmentTypeRemote

internal object MediaAttachmentMapper {

    fun map(from: MediaAttachmentResponse): MediaAttachment =
        MediaAttachment(
            id = from.id,
            type = when (from.type) {
                MediaAttachmentTypeRemote.IMAGE -> MediaAttachmentType.IMAGE
                MediaAttachmentTypeRemote.GIF -> MediaAttachmentType.GIF
                MediaAttachmentTypeRemote.VIDEO -> MediaAttachmentType.VIDEO
                MediaAttachmentTypeRemote.AUDIO -> MediaAttachmentType.AUDIO
                MediaAttachmentTypeRemote.UNKNOWN -> MediaAttachmentType.UNKNOWN
            },
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
