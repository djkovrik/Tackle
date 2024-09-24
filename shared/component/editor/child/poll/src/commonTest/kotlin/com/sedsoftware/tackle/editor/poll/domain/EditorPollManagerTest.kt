package com.sedsoftware.tackle.editor.poll.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.poll.Instances
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class EditorPollManagerTest {

    private val manager: EditorPollManager = EditorPollManager()

    @Test
    fun `getAvailableDurations should check min max bounds`() = runTest {
        // given
        val config = Instances.config
        // when
        val result: List<PollDuration> = manager.getAvailableDurations(config)
        // then
        assertThat(result).isNotEmpty()
        assertThat(result.first()).isEqualTo(PollDuration.TEN_MINUTES)
        assertThat(result.last()).isEqualTo(PollDuration.TWO_DAYS)
    }
}
