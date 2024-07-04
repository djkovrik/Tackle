package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.domain.model.ScheduledStatus
import com.sedsoftware.tackle.domain.model.ScheduledStatusParams
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.network.response.ScheduledStatusParamsResponse
import com.sedsoftware.tackle.network.response.ScheduledStatusResponse
import com.sedsoftware.tackle.network.response.type.StatusVisibilityRemote.DIRECT
import com.sedsoftware.tackle.network.response.type.StatusVisibilityRemote.PRIVATE
import com.sedsoftware.tackle.network.response.type.StatusVisibilityRemote.PUBLIC
import com.sedsoftware.tackle.network.response.type.StatusVisibilityRemote.UNKNOWN
import com.sedsoftware.tackle.network.response.type.StatusVisibilityRemote.UNLISTED
import com.sedsoftware.tackle.utils.extension.toLocalDateTime

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
                PUBLIC -> StatusVisibility.PUBLIC
                UNLISTED -> StatusVisibility.UNLISTED
                PRIVATE -> StatusVisibility.PRIVATE
                DIRECT -> StatusVisibility.DIRECT
                UNKNOWN -> StatusVisibility.UNKNOWN
            },
            inReplyToId = from.inReplyToId,
            language = from.language,
            applicationId = from.applicationId,
            idempotency = from.idempotency,
            withRateLimit = from.withRateLimit,
            mediaAttachments = from.mediaAttachments.map(MediaAttachmentMapper::map),
        )
}
