package com.sedsoftware.tackle.editor.extension

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.sedsoftware.tackle.editor.store.EditorTabStore
import com.sedsoftware.tackle.editor.stubs.EmojiStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class StringExtTest {

    @Test
    fun `insertEmoji should update state text after emoji insert at the end when far from the limit`() = runTest {
        // given
        val emoji = EmojiStub.single
        val enteredText = "Some input text "
        val expectedText = "Some input text :${emoji.shortcode}:"
        val currentSelection = enteredText.length to enteredText.length
        val limit = 123

        val state = EditorTabStore.State(
            statusText = enteredText,
            statusTextSelection = currentSelection,
            statusCharactersLeft = limit - enteredText.length,
            statusCharactersLimit = limit,
        )

        // when
        val result = enteredText.insertEmoji(emoji, state)
        // then
        assertThat(result).isEqualTo(expectedText)
    }

    @Test
    fun `insertEmoji should update state text after emoji insert in the middle when far from the limit`() = runTest {
        // given
        val emoji = EmojiStub.single
        val enteredText = "Some input text"
        val expectedText = "Some:${emoji.shortcode}: input text"
        val currentSelection = 4 to 4
        val limit = 123

        val state = EditorTabStore.State(
            statusText = enteredText,
            statusTextSelection = currentSelection,
            statusCharactersLeft = limit - enteredText.length,
            statusCharactersLimit = limit,
        )

        // when
        val result = enteredText.insertEmoji(emoji, state)
        // then
        assertThat(result).isEqualTo(expectedText)
    }

    @Test
    fun `insertEmoji should update state text after emoji insert at the end when near the limit`() = runTest {
        // given
        val emoji = EmojiStub.single
        val enteredText = "Some input text "
        val expectedText = "Some input text :${emoji.shortcode}:".take(20)
        val currentSelection = enteredText.length to enteredText.length
        val limit = 20

        val state = EditorTabStore.State(
            statusText = enteredText,
            statusTextSelection = currentSelection,
            statusCharactersLeft = limit - enteredText.length,
            statusCharactersLimit = limit,
        )

        // when
        val result = enteredText.insertEmoji(emoji, state)
        // then
        assertThat(result).isEqualTo(expectedText)
    }

    @Test
    fun `insertEmoji should update state text after emoji insert in the middle when near the limit`() = runTest {
        // given
        val emoji = EmojiStub.single
        val enteredText = "Some input text"
        val expectedText = "Some:${emoji.shortcode}: input text".take(20)
        val currentSelection = 4 to 4
        val limit = 20

        val state = EditorTabStore.State(
            statusText = enteredText,
            statusTextSelection = currentSelection,
            statusCharactersLeft = limit - enteredText.length,
            statusCharactersLimit = limit,
        )

        // when
        val result = enteredText.insertEmoji(emoji, state)
        // then
        assertThat(result).isEqualTo(expectedText)
    }

    @Test
    fun `getNewPosition should calculate position text after emoji insert at the end when far from the limit`() = runTest {
        // given
        val emoji = EmojiStub.single
        val enteredText = "Some input text "
        val currentSelection = enteredText.length to enteredText.length
        val limit = 123
        val additionalLength = 2

        val state = EditorTabStore.State(
            statusText = enteredText,
            statusTextSelection = currentSelection,
            statusCharactersLeft = limit - enteredText.length,
            statusCharactersLimit = limit,
        )

        // when
        val result = enteredText.getNewPosition(emoji, state)
        // then
        assertThat(result.first).isEqualTo(currentSelection.second + emoji.shortcode.length + additionalLength)
        assertThat(result.second).isEqualTo(currentSelection.second + emoji.shortcode.length + additionalLength)
    }

    @Test
    fun `getNewPosition should calculate position text after emoji insert in the middle when far from the limit`() = runTest {
        // given
        val emoji = EmojiStub.single
        val enteredText = "Some input text"
        val currentSelection = 4 to 4
        val limit = 123
        val additionalLength = 2

        val state = EditorTabStore.State(
            statusText = enteredText,
            statusTextSelection = currentSelection,
            statusCharactersLeft = limit - enteredText.length,
            statusCharactersLimit = limit,
        )

        // when
        val result = enteredText.getNewPosition(emoji, state)
        assertThat(result.first).isEqualTo(currentSelection.second + emoji.shortcode.length + additionalLength)
        assertThat(result.second).isEqualTo(currentSelection.second + emoji.shortcode.length + additionalLength)
    }

    @Test
    fun `getNewPosition should calculate position text after emoji insert at the end when near the limit`() = runTest {
// given
        val emoji = EmojiStub.single
        val enteredText = "Some input text "
        val currentSelection = enteredText.length to enteredText.length
        val limit = 20

        val state = EditorTabStore.State(
            statusText = enteredText,
            statusTextSelection = currentSelection,
            statusCharactersLeft = limit - enteredText.length,
            statusCharactersLimit = limit,
        )

        // when
        val result = enteredText.getNewPosition(emoji, state)
        // then
        assertThat(result.first).isEqualTo(limit)
        assertThat(result.second).isEqualTo(limit)
    }

    @Test
    fun `getNewPosition should calculate position text after emoji insert in the middle when near the limit`() = runTest {
        // given
        val emoji = EmojiStub.single
        val enteredText = "Some input text"
        val currentSelection = 4 to 4
        val limit = 20
        val additionalLength = 2

        val state = EditorTabStore.State(
            statusText = enteredText,
            statusTextSelection = currentSelection,
            statusCharactersLeft = limit - enteredText.length,
            statusCharactersLimit = limit,
        )

        // when
        val result = enteredText.getNewPosition(emoji, state)
        assertThat(result.first).isEqualTo(currentSelection.second + emoji.shortcode.length + additionalLength)
        assertThat(result.second).isEqualTo(currentSelection.second + emoji.shortcode.length + additionalLength)
    }

    @Test
    fun `getNewLength should calculate new input length when far from the limit`() {
        // given
        val emoji = EmojiStub.single
        val enteredText = "Some input text "
        val currentSelection = enteredText.length to enteredText.length
        val limit = 123
        val additionalLength = 2

        val state = EditorTabStore.State(
            statusText = enteredText,
            statusTextSelection = currentSelection,
            statusCharactersLeft = limit - enteredText.length,
            statusCharactersLimit = limit,
        )

        // when
        val result = enteredText.getNewLength(emoji, state)
        // then
        assertThat(result).isEqualTo(enteredText.length + emoji.shortcode.length + additionalLength)
    }

    @Test
    fun `getNewLength should calculate new input length when near the limit`() {
        // given
        val emoji = EmojiStub.single
        val enteredText = "Some input text "
        val currentSelection = enteredText.length to enteredText.length
        val limit = 20

        val state = EditorTabStore.State(
            statusText = enteredText,
            statusTextSelection = currentSelection,
            statusCharactersLeft = limit - enteredText.length,
            statusCharactersLimit = limit,
        )

        // when
        val result = enteredText.getNewLength(emoji, state)
        // then
        assertThat(result).isEqualTo(limit)
    }
}
