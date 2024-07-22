package com.sedsoftware.tackle.editor.poll.extension

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.sedsoftware.tackle.editor.poll.model.PollChoiceOption
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ExtensionsTest {

    @Test
    fun `applyInput should update list item`() = runTest {
        // given
        val options = listOf(
            PollChoiceOption(id = "1", text = "abc"),
            PollChoiceOption(id = "2", text = "def"),
            PollChoiceOption(id = "3", text = "ghi"),
            PollChoiceOption(id = "4", text = "jkl"),
        )
        val targetId = "2"
        val newInput = "Test input"
        // when
        val newOptions = options.applyInput(targetId, newInput)
        val updatedItem = newOptions.find { it.id == targetId }
        // then
        assertThat(updatedItem?.text).isEqualTo(newInput)
    }
}
