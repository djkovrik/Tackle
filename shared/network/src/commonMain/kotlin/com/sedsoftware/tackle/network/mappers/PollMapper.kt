package com.sedsoftware.tackle.network.mappers

import com.sedsoftware.tackle.network.model.Poll
import com.sedsoftware.tackle.network.model.PollOption
import com.sedsoftware.tackle.network.response.PollOptionResponse
import com.sedsoftware.tackle.network.response.PollResponse
import com.sedsoftware.tackle.utils.toLocalDateTime

internal object PollMapper {

    fun map(from: PollResponse): Poll =
        Poll(
            id = from.id,
            expiresAt = from.expiresAt.toLocalDateTime(),
            expired = from.expired,
            multiple = from.multiple,
            votesCount = from.votesCount,
            votersCount = from.votersCount,
            options = from.options.map(PollMapper::mapPollOption),
            emojis = from.emojis.map(EmojisMapper::map),
            voted = from.voted,
            hideTotals = from.hideTotals,
            ownVotes = from.ownVotes,
        )

    private fun mapPollOption(from: PollOptionResponse): PollOption =
        PollOption(from.title, from.votesCount)
}
