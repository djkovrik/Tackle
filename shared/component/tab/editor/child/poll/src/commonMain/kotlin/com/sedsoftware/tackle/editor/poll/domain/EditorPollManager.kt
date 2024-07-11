package com.sedsoftware.tackle.editor.poll.domain

import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.poll.model.PollDuration

internal class EditorPollManager {

    fun getAvailableDurations(config: Instance.Config): List<PollDuration> {
        val minExpiration: Long = config.polls.minExpiration
        val maxExpiration: Long = config.polls.maxExpiration

        return PollDuration.entries
            .filter { it.seconds in minExpiration..maxExpiration }
            .sortedBy { it.seconds }
    }
}
