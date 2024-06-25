package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.network.model.ScheduledStatus
import com.sedsoftware.tackle.network.model.ScheduledStatusParams
import com.sedsoftware.tackle.network.model.type.StatusVisibility
import com.sedsoftware.tackle.network.response.ScheduledStatusParamsResponse
import com.sedsoftware.tackle.network.response.ScheduledStatusResponse
import com.sedsoftware.tackle.network.response.type.StatusVisibilityRemote
import com.sedsoftware.tackle.utils.toLocalDateTime

internal object ScheduledStatusMapper {

    fun map(from: ScheduledStatusResponse): ScheduledStatus =
        ScheduledStatus(
            id = from.id,
            scheduledAt = from.scheduledAt.toLocalDateTime(),
            params = mapParams(from.params),
        )

    private fun mapParams(from: ScheduledStatusParamsResponse): ScheduledStatusParams =
        ScheduledStatusParams(
            text = from.text,
            poll = from.poll?.let { PollMapper.map(it) },
            mediaIds = from.mediaIds,
            sensitive = from.sensitive,
            spoilerText = from.spoilerText,
            visibility = when (from.visibility) {
                StatusVisibilityRemote.PUBLIC -> StatusVisibility.PUBLIC
                StatusVisibilityRemote.UNLISTED -> StatusVisibility.UNLISTED
                StatusVisibilityRemote.PRIVATE -> StatusVisibility.PRIVATE
                StatusVisibilityRemote.DIRECT -> StatusVisibility.DIRECT
                StatusVisibilityRemote.UNKNOWN -> StatusVisibility.UNKNOWN
            },
            inReplyToId = from.inReplyToId,
            language = from.language,
            applicationId = from.applicationId,
            idempotency = from.idempotency,
            withRateLimit = from.withRateLimit,
            mediaAttachments = from.mediaAttachments.map(MediaAttachmentMapper::map),
        )
}
