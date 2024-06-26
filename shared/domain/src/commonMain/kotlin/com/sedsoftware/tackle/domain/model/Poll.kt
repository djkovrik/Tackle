package com.sedsoftware.tackle.domain.model

import kotlinx.datetime.LocalDateTime

data class Poll(
    val id: String,
    val expiresAt: LocalDateTime,
    val expired: Boolean,
    val multiple: Boolean,
    val votesCount: Long,
    val votersCount: Long,
    val options: List<PollOption>,
    val emojis: List<CustomEmoji>,
    val voted: Boolean,
    val hideTotals: Boolean,
    val ownVotes: List<Int>
)

data class PollOption(
    val title: String,
    val votesCount: Long,
)
