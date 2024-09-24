package com.sedsoftware.tackle.editor.extension

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.sedsoftware.tackle.editor.Instances
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.model.EditorInputHintRequest
import com.sedsoftware.tackle.editor.store.EditorStore
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class StringExtTest {

    @Test
    fun `insertEmoji should update state text after emoji insert at the end when far from the limit`() = runTest {
        // given
        val emoji = Instances.emoji
        val enteredText = "Some input text "
        val expectedText = "Some input text :${emoji.shortcode}:"
        val currentSelection = enteredText.length to enteredText.length
        val limit = 123

        val state = EditorStore.State(
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
        val emoji = Instances.emoji
        val enteredText = "Some input text"
        val expectedText = "Some:${emoji.shortcode}: input text"
        val currentSelection = 4 to 4
        val limit = 123

        val state = EditorStore.State(
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
        val emoji = Instances.emoji
        val enteredText = "Some input text "
        val expectedText = "Some input text :${emoji.shortcode}:".take(20)
        val currentSelection = enteredText.length to enteredText.length
        val limit = 20

        val state = EditorStore.State(
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
        val emoji = Instances.emoji
        val enteredText = "Some input text"
        val expectedText = "Some:${emoji.shortcode}: input text".take(20)
        val currentSelection = 4 to 4
        val limit = 20

        val state = EditorStore.State(
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
        val emoji = Instances.emoji
        val enteredText = "Some input text "
        val currentSelection = enteredText.length to enteredText.length
        val limit = 123
        val additionalLength = 2

        val state = EditorStore.State(
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
        val emoji = Instances.emoji
        val enteredText = "Some input text"
        val currentSelection = 4 to 4
        val limit = 123
        val additionalLength = 2

        val state = EditorStore.State(
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
        val emoji = Instances.emoji
        val enteredText = "Some input text "
        val currentSelection = enteredText.length to enteredText.length
        val limit = 20

        val state = EditorStore.State(
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
        val emoji = Instances.emoji
        val enteredText = "Some input text"
        val currentSelection = 4 to 4
        val limit = 20
        val additionalLength = 2

        val state = EditorStore.State(
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
        val emoji = Instances.emoji
        val enteredText = "Some input text "
        val currentSelection = enteredText.length to enteredText.length
        val limit = 123
        val additionalLength = 2

        val state = EditorStore.State(
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
        val emoji = Instances.emoji
        val enteredText = "Some input text "
        val currentSelection = enteredText.length to enteredText.length
        val limit = 20

        val state = EditorStore.State(
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

    @Test
    fun `account insert should update state text and position after insert at the end when far from the limit`() {
        // given
        val text = "Some text @ban"
        val hint = EditorInputHintItem.Account("", "banana", "")
        val state1 = EditorStore.State(
            statusText = text,
            statusTextSelection = 14 to 14,
            currentSuggestionRequest = EditorInputHintRequest.Accounts("@ban"),
            statusCharactersLimit = 500,
        )
        val state2 = state1.copy(
            statusTextSelection = 13 to 13,
        )
        val state3 = state1.copy(
            statusTextSelection = 12 to 12,
        )
        val expectedText = "Some text @banana"
        val expectedSelection = 17 to 17
        val expectedLength = expectedText.length
        // when
        val newStatusText1 = text.insertHint(hint, state1)
        val newStatusText2 = text.insertHint(hint, state2)
        val newStatusText3 = text.insertHint(hint, state3)
        val newSelection1 = text.getNewPosition(hint, state1)
        val newSelection2 = text.getNewPosition(hint, state2)
        val newSelection3 = text.getNewPosition(hint, state3)
        val newLength1 = text.getNewLength(hint, state1)
        val newLength2 = text.getNewLength(hint, state2)
        val newLength3 = text.getNewLength(hint, state3)

        // then
        assertThat(newStatusText1).isEqualTo(expectedText)
        assertThat(newStatusText2).isEqualTo(expectedText)
        assertThat(newStatusText3).isEqualTo(expectedText)
        assertThat(newSelection1).isEqualTo(expectedSelection)
        assertThat(newSelection2).isEqualTo(expectedSelection)
        assertThat(newSelection3).isEqualTo(expectedSelection)
        assertThat(newLength1).isEqualTo(expectedLength)
        assertThat(newLength2).isEqualTo(expectedLength)
        assertThat(newLength3).isEqualTo(expectedLength)
    }

    @Test
    fun `emoji insert should update state text and position after insert at the end when far from the limit`() {
        // given
        val text = "Some text :ban"
        val hint = EditorInputHintItem.Emoji("banana", "")
        val state1 = EditorStore.State(
            statusText = text,
            statusTextSelection = 14 to 14,
            currentSuggestionRequest = EditorInputHintRequest.Emojis(":ban"),
            statusCharactersLimit = 500,
        )
        val state2 = state1.copy(
            statusTextSelection = 13 to 13,
        )
        val state3 = state1.copy(
            statusTextSelection = 12 to 12,
        )
        val expectedText = "Some text :banana:"
        val expectedSelection = 18 to 18
        val expectedLength = expectedText.length
        // when
        val newStatusText1 = text.insertHint(hint, state1)
        val newStatusText2 = text.insertHint(hint, state2)
        val newStatusText3 = text.insertHint(hint, state3)
        val newSelection1 = text.getNewPosition(hint, state1)
        val newSelection2 = text.getNewPosition(hint, state2)
        val newSelection3 = text.getNewPosition(hint, state3)
        val newLength1 = text.getNewLength(hint, state1)
        val newLength2 = text.getNewLength(hint, state2)
        val newLength3 = text.getNewLength(hint, state3)
        // then
        assertThat(newStatusText1).isEqualTo(expectedText)
        assertThat(newStatusText2).isEqualTo(expectedText)
        assertThat(newStatusText3).isEqualTo(expectedText)
        assertThat(newSelection1).isEqualTo(expectedSelection)
        assertThat(newSelection2).isEqualTo(expectedSelection)
        assertThat(newSelection3).isEqualTo(expectedSelection)
        assertThat(newLength1).isEqualTo(expectedLength)
        assertThat(newLength2).isEqualTo(expectedLength)
        assertThat(newLength3).isEqualTo(expectedLength)
    }

    @Test
    fun `hashTag insert should update state text and position after insert at the end when far from the limit`() {
        // given
        val text = "Some text #ban"
        val hint = EditorInputHintItem.HashTag("banana")
        val state1 = EditorStore.State(
            statusText = text,
            statusTextSelection = 14 to 14,
            currentSuggestionRequest = EditorInputHintRequest.HashTags("#ban"),
            statusCharactersLimit = 500,
        )
        val state2 = state1.copy(
            statusTextSelection = 13 to 13,
        )
        val state3 = state1.copy(
            statusTextSelection = 12 to 12,
        )
        val expectedText = "Some text #banana"
        val expectedSelection = 17 to 17
        val expectedLength = expectedText.length
        // when
        val newStatusText1 = text.insertHint(hint, state1)
        val newStatusText2 = text.insertHint(hint, state2)
        val newStatusText3 = text.insertHint(hint, state3)
        val newSelection1 = text.getNewPosition(hint, state1)
        val newSelection2 = text.getNewPosition(hint, state2)
        val newSelection3 = text.getNewPosition(hint, state3)
        val newLength1 = text.getNewLength(hint, state1)
        val newLength2 = text.getNewLength(hint, state2)
        val newLength3 = text.getNewLength(hint, state3)
        // then
        assertThat(newStatusText1).isEqualTo(expectedText)
        assertThat(newStatusText2).isEqualTo(expectedText)
        assertThat(newStatusText3).isEqualTo(expectedText)
        assertThat(newSelection1).isEqualTo(expectedSelection)
        assertThat(newSelection2).isEqualTo(expectedSelection)
        assertThat(newSelection3).isEqualTo(expectedSelection)
        assertThat(newLength1).isEqualTo(expectedLength)
        assertThat(newLength2).isEqualTo(expectedLength)
        assertThat(newLength3).isEqualTo(expectedLength)
    }

    @Test
    fun `account insert should update state text and position after insert in the middle when far from the limit`() {
        // given
        val text = "Some text @ban and some text"
        val hint = EditorInputHintItem.Account("", "banana", "")
        val state1 = EditorStore.State(
            statusText = text,
            statusTextSelection = 14 to 14,
            currentSuggestionRequest = EditorInputHintRequest.Accounts("@ban"),
            statusCharactersLimit = 500,
        )
        val state2 = state1.copy(
            statusTextSelection = 13 to 13,
        )
        val state3 = state1.copy(
            statusTextSelection = 12 to 12,
        )
        val expectedText = "Some text @banana and some text"
        val expectedSelection = 17 to 17
        val expectedLength = expectedText.length
        // when
        val newStatusText1 = text.insertHint(hint, state1)
        val newStatusText2 = text.insertHint(hint, state2)
        val newStatusText3 = text.insertHint(hint, state3)
        val newSelection1 = text.getNewPosition(hint, state1)
        val newSelection2 = text.getNewPosition(hint, state2)
        val newSelection3 = text.getNewPosition(hint, state3)
        val newLength1 = text.getNewLength(hint, state1)
        val newLength2 = text.getNewLength(hint, state2)
        val newLength3 = text.getNewLength(hint, state3)

        // then
        assertThat(newStatusText1).isEqualTo(expectedText)
        assertThat(newStatusText2).isEqualTo(expectedText)
        assertThat(newStatusText3).isEqualTo(expectedText)
        assertThat(newSelection1).isEqualTo(expectedSelection)
        assertThat(newSelection2).isEqualTo(expectedSelection)
        assertThat(newSelection3).isEqualTo(expectedSelection)
        assertThat(newLength1).isEqualTo(expectedLength)
        assertThat(newLength2).isEqualTo(expectedLength)
        assertThat(newLength3).isEqualTo(expectedLength)
    }

    @Test
    fun `emoji insert should update state text and position after insert in the middle when far from the limit`() {
        // given
        val text = "Some text :ban and some text"
        val hint = EditorInputHintItem.Emoji("banana", "")
        val state1 = EditorStore.State(
            statusText = text,
            statusTextSelection = 14 to 14,
            currentSuggestionRequest = EditorInputHintRequest.Emojis(":ban"),
            statusCharactersLimit = 500,
        )
        val state2 = state1.copy(
            statusTextSelection = 13 to 13,
        )
        val state3 = state1.copy(
            statusTextSelection = 12 to 12,
        )
        val expectedText = "Some text :banana: and some text"
        val expectedSelection = 18 to 18
        val expectedLength = expectedText.length
        // when
        val newStatusText1 = text.insertHint(hint, state1)
        val newStatusText2 = text.insertHint(hint, state2)
        val newStatusText3 = text.insertHint(hint, state3)
        val newSelection1 = text.getNewPosition(hint, state1)
        val newSelection2 = text.getNewPosition(hint, state2)
        val newSelection3 = text.getNewPosition(hint, state3)
        val newLength1 = text.getNewLength(hint, state1)
        val newLength2 = text.getNewLength(hint, state2)
        val newLength3 = text.getNewLength(hint, state3)
        // then
        assertThat(newStatusText1).isEqualTo(expectedText)
        assertThat(newStatusText2).isEqualTo(expectedText)
        assertThat(newStatusText3).isEqualTo(expectedText)
        assertThat(newSelection1).isEqualTo(expectedSelection)
        assertThat(newSelection2).isEqualTo(expectedSelection)
        assertThat(newSelection3).isEqualTo(expectedSelection)
        assertThat(newLength1).isEqualTo(expectedLength)
        assertThat(newLength2).isEqualTo(expectedLength)
        assertThat(newLength3).isEqualTo(expectedLength)
    }

    @Test
    fun `hashTag insert should update state text and position after insert in the middle when far from the limit`() {
        // given
        val text = "Some text #ban and some text"
        val hint = EditorInputHintItem.HashTag("banana")
        val state1 = EditorStore.State(
            statusText = text,
            statusTextSelection = 14 to 14,
            currentSuggestionRequest = EditorInputHintRequest.HashTags("#ban"),
            statusCharactersLimit = 500,
        )
        val state2 = state1.copy(
            statusTextSelection = 13 to 13,
        )
        val state3 = state1.copy(
            statusTextSelection = 12 to 12,
        )
        val expectedText = "Some text #banana and some text"
        val expectedSelection = 17 to 17
        val expectedLength = expectedText.length
        // when
        val newStatusText1 = text.insertHint(hint, state1)
        val newStatusText2 = text.insertHint(hint, state2)
        val newStatusText3 = text.insertHint(hint, state3)
        val newSelection1 = text.getNewPosition(hint, state1)
        val newSelection2 = text.getNewPosition(hint, state2)
        val newSelection3 = text.getNewPosition(hint, state3)
        val newLength1 = text.getNewLength(hint, state1)
        val newLength2 = text.getNewLength(hint, state2)
        val newLength3 = text.getNewLength(hint, state3)
        // then
        assertThat(newStatusText1).isEqualTo(expectedText)
        assertThat(newStatusText2).isEqualTo(expectedText)
        assertThat(newStatusText3).isEqualTo(expectedText)
        assertThat(newSelection1).isEqualTo(expectedSelection)
        assertThat(newSelection2).isEqualTo(expectedSelection)
        assertThat(newSelection3).isEqualTo(expectedSelection)
        assertThat(newLength1).isEqualTo(expectedLength)
        assertThat(newLength2).isEqualTo(expectedLength)
        assertThat(newLength3).isEqualTo(expectedLength)
    }

    @Test
    fun `account insert should update state text and position after insert at the end when near the limit`() {
        // given
        val text = "Some text here @ban"
        val hint = EditorInputHintItem.Account("", "banana", "")
        val state = EditorStore.State(
            statusText = text,
            statusTextSelection = 19 to 19,
            currentSuggestionRequest = EditorInputHintRequest.Accounts("@ban"),
            statusCharactersLimit = 20,
        )
        val expectedText = "Some text here @banana".take(20)
        val expectedSelection = 20 to 20
        val expectedLength = expectedText.length
        // when
        val newStatusText = text.insertHint(hint, state)
        val newSelection = text.getNewPosition(hint, state)
        val newLength = text.getNewLength(hint, state)

        // then
        assertThat(newStatusText).isEqualTo(expectedText)
        assertThat(newSelection).isEqualTo(expectedSelection)
        assertThat(newLength).isEqualTo(expectedLength)
    }

    @Test
    fun `emoji insert should update state text and position after insert at the end when near the limit`() {
        // given
        val text = "Some text here :ban"
        val hint = EditorInputHintItem.Emoji("banana", "")
        val state = EditorStore.State(
            statusText = text,
            statusTextSelection = 19 to 19,
            currentSuggestionRequest = EditorInputHintRequest.Emojis(":ban"),
            statusCharactersLimit = 20,
        )
        val expectedText = "Some text here :banana:".take(20)
        val expectedSelection = 20 to 20
        val expectedLength = expectedText.length
        // when
        val newStatusText = text.insertHint(hint, state)
        val newSelection = text.getNewPosition(hint, state)
        val newLength = text.getNewLength(hint, state)

        // then
        assertThat(newStatusText).isEqualTo(expectedText)
        assertThat(newSelection).isEqualTo(expectedSelection)
        assertThat(newLength).isEqualTo(expectedLength)
    }

    @Test
    fun `hashTag insert should update state text and position after insert at the end when near the limit`() {
        // given
        val text = "Some text here #ban"
        val hint = EditorInputHintItem.HashTag("banana")
        val state = EditorStore.State(
            statusText = text,
            statusTextSelection = 19 to 19,
            currentSuggestionRequest = EditorInputHintRequest.HashTags("#ban"),
            statusCharactersLimit = 20,
        )
        val expectedText = "Some text here #banana".take(20)
        val expectedSelection = 20 to 20
        val expectedLength = expectedText.length
        // when
        val newStatusText = text.insertHint(hint, state)
        val newSelection = text.getNewPosition(hint, state)
        val newLength = text.getNewLength(hint, state)

        // then
        assertThat(newStatusText).isEqualTo(expectedText)
        assertThat(newSelection).isEqualTo(expectedSelection)
        assertThat(newLength).isEqualTo(expectedLength)
    }
    
    @Test
    fun `getNewLength returns hint length for none request`() = runTest {
        // given
        val currentText = ""
        val state = EditorStore.State(
            statusText = currentText,
            currentSuggestionRequest = EditorInputHintRequest.None,
            statusCharactersLimit = 500,
        )
        val hintText = "test"
        val hint = EditorInputHintItem.HashTag(hintText)
        // when
        val newLength = currentText.getNewLength(hint, state)
        // then
        assertThat(newLength).isEqualTo(hintText.length + 1)
    }
}
