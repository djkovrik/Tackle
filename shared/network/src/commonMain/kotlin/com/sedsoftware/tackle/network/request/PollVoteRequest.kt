package com.sedsoftware.tackle.network.request

import kotlinx.serialization.Serializable

@Serializable
internal class PollVoteRequest(
    val choices: List<Int>,
)
