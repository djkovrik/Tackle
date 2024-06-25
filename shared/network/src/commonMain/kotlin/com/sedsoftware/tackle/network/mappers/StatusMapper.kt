package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.network.model.Status
import com.sedsoftware.tackle.network.model.StatusMention
import com.sedsoftware.tackle.network.model.StatusTag
import com.sedsoftware.tackle.network.model.type.StatusVisibility
import com.sedsoftware.tackle.network.response.StatusMentionResponse
import com.sedsoftware.tackle.network.response.StatusResponse
import com.sedsoftware.tackle.network.response.StatusTagResponse
import com.sedsoftware.tackle.network.response.type.StatusVisibilityRemote
import com.sedsoftware.tackle.utils.toLocalDateTime

internal object StatusMapper {

    fun map(from: StatusResponse): Status =
        Status(
            id = from.id,
            uri = from.uri,
            createdAt = from.createdAt.toLocalDateTime(),
            account = AccountMapper.map(from.account),
            content = from.content,
            visibility = when (from.visibility) {
                StatusVisibilityRemote.PUBLIC -> StatusVisibility.PUBLIC
                StatusVisibilityRemote.UNLISTED -> StatusVisibility.UNLISTED
                StatusVisibilityRemote.PRIVATE -> StatusVisibility.PRIVATE
                StatusVisibilityRemote.DIRECT -> StatusVisibility.DIRECT
                StatusVisibilityRemote.UNKNOWN -> StatusVisibility.UNKNOWN
            },
            sensitive = from.sensitive,
            spoilerText = from.spoilerText,
            mediaAttachments = from.mediaAttachments.map(MediaAttachmentMapper::map),
            application = from.application?.let { ApplicationMapper.map(it) },
            mentions = from.mentions.map(::mapMention),
            tags = from.tags.map(::mapTag),
            emojis = from.emojis.map(EmojisMapper::map),
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
            editedAt = from.editedAt.toLocalDateTime(),
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
