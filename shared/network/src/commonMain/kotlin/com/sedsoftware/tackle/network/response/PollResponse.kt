package com.sedsoftware.tackle.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class PollResponse(
    @SerialName("id") val id: String = "",
    @SerialName("expires_at") val expiresAt: String = "",
    @SerialName("expired") val expired: Boolean = false,
    @SerialName("multiple") val multiple: Boolean = false,
    @SerialName("votes_count") val votesCount: Long = 0L,
    @SerialName("voters_count") val votersCount: Long = 0L,
    @SerialName("options") val options: List<PollOptionResponse> = emptyList(),
    @SerialName("emojis") val emojis: List<CustomEmojiResponse> = emptyList(),
    @SerialName("voted") val voted: Boolean = false,
    @SerialName("own_votes") val ownVotes: List<Int> = emptyList(),
)

@Serializable
internal class PollOptionResponse(
    @SerialName("title") val title: String = "",
    @SerialName("votes_count") val votesCount: Long = 0L,
)
