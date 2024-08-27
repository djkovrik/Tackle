package com.sedsoftware.tackle.network.mapper

import com.sedsoftware.tackle.domain.model.ScheduledStatus
import com.sedsoftware.tackle.domain.model.ScheduledStatusParams
import com.sedsoftware.tackle.domain.model.ScheduledStatusPoll
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.network.response.ScheduledStatusParamsResponse
import com.sedsoftware.tackle.network.response.ScheduledStatusResponse
import com.sedsoftware.tackle.utils.extension.toLocalDateTime

internal object ScheduledStatusMapper {

    fun map(from: ScheduledStatusResponse): ScheduledStatus =
        ScheduledStatus(
            id = from.id,
            scheduledAt = from.scheduledAt.toLocalDateTime(),
            params = mapParams(from.params),
            mediaAttachments = from.mediaAttachments.map { MediaAttachmentMapper.map(it) },
        )

    private fun mapParams(from: ScheduledStatusParamsResponse): ScheduledStatusParams =
        ScheduledStatusParams(
            text = from.text,
            poll = from.poll?.let { ScheduledStatusPoll(it.multiple, it.hideTotals, it.expiresIn, it.options) },
            mediaIds = from.mediaIds,
            sensitive = from.sensitive,
            spoilerText = from.spoilerText,
            visibility = StatusVisibility.entries.firstOrNull { it.name.lowercase() == from.visibility } ?: StatusVisibility.UNKNOWN,
            inReplyToId = from.inReplyToId,
            language = from.language,
            applicationId = from.applicationId,
            idempotency = from.idempotency,
            withRateLimit = from.withRateLimit,
        )
}
