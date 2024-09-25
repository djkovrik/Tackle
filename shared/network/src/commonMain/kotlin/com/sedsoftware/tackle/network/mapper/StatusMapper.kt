package com.sedsoftware.tackle.network.mapper

import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.StatusMention
import com.sedsoftware.tackle.domain.model.StatusTag
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.network.response.StatusMentionResponse
import com.sedsoftware.tackle.network.response.StatusResponse
import com.sedsoftware.tackle.network.response.StatusTagResponse
import com.sedsoftware.tackle.utils.DateTimeUtils
import com.sedsoftware.tackle.utils.extension.toLocalDateTimeCustom
import kotlinx.datetime.Clock.System
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal object StatusMapper {

    private val now: LocalDateTime by lazy {
        System.now().toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
    }

    fun map(from: StatusResponse): Status =
        Status(
            id = from.id,
            uri = from.uri,
            createdAt = from.createdAt.toLocalDateTimeCustom(),
            createdAtShort = DateTimeUtils.getDateShortLabel(now, from.createdAt.toLocalDateTimeCustom()),
            createdAtPretty = DateTimeUtils.prettify(from.createdAt.toLocalDateTimeCustom()),
            account = AccountMapper.map(from.account),
            content = from.content,
            visibility = StatusVisibility.entries.firstOrNull { it.name.lowercase() == from.visibility } ?: StatusVisibility.UNKNOWN,
            sensitive = from.sensitive,
            spoilerText = from.spoilerText,
            mediaAttachments = from.mediaAttachments.map(MediaAttachmentMapper::map),
            application = from.application?.let { ApplicationMapper.map(it) },
            mentions = from.mentions.map(::mapMention),
            tags = from.tags.map(::mapTag),
            emojis = CustomEmojiMapper.map(from.emojis),
            reblogsCount = from.reblogsCount,
            favouritesCount = from.favouritesCount,
            repliesCount = from.repliesCount,
            url = from.url,
            inReplyToId = from.inReplyToId,
            inReplyToAccountId = from.inReplyToAccountId,
            reblog = from.reblog?.let { map(it) },
            poll = from.poll?.let { PollMapper.map(it) },
            card = from.card?.let { PreviewCardMapper.map(it) },
            language = from.language,
            text = from.text,
            editedAt = from.editedAt?.toLocalDateTimeCustom(),
            editedAtShort = from.editedAt?.toLocalDateTimeCustom()?.let { DateTimeUtils.getDateShortLabel(now, it) },
            editedAtPretty = from.editedAt?.toLocalDateTimeCustom()?.let { DateTimeUtils.prettify(it) },
            favourited = from.favourited,
            reblogged = from.reblogged,
            muted = from.muted,
            bookmarked = from.bookmarked,
            pinned = from.pinned,
            filtered = from.filtered.map(FilterResultMapper::map),
        )

    private fun mapMention(from: StatusMentionResponse): StatusMention =
        StatusMention(
            id = from.id,
            username = from.username,
            url = from.url,
            acct = from.acct,
        )

    private fun mapTag(from: StatusTagResponse): StatusTag =
        StatusTag(
            name = from.name,
            url = from.url,
        )
}
