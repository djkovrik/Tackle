package com.sedsoftware.tackle.network.mapper

import com.sedsoftware.tackle.domain.model.Poll
import com.sedsoftware.tackle.domain.model.PollOption
import com.sedsoftware.tackle.network.response.PollOptionResponse
import com.sedsoftware.tackle.network.response.PollResponse
import com.sedsoftware.tackle.utils.DateTimeUtils
import com.sedsoftware.tackle.utils.extension.toLocalDateTimeCustom

internal object PollMapper {

    fun map(from: PollResponse): Poll =
        Poll(
            id = from.id,
            expiresAt = from.expiresAt.toLocalDateTimeCustom(),
            expiresAtStr = DateTimeUtils.prettify(from.expiresAt.toLocalDateTimeCustom()),
            expired = from.expired,
            multiple = from.multiple,
            votesCount = from.votesCount,
            votersCount = from.votersCount,
            options = from.options.map(PollMapper::mapPollOption),
            emojis = CustomEmojiMapper.map(from.emojis),
            voted = from.voted,
            hideTotals = from.hideTotals,
            ownVotes = from.ownVotes,
        )

    private fun mapPollOption(from: PollOptionResponse): PollOption =
        PollOption(from.title, from.votesCount)
}
