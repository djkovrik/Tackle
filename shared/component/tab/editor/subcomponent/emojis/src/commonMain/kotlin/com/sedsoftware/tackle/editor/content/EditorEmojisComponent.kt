package com.sedsoftware.tackle.editor.content

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.CustomEmoji

interface EditorEmojisComponent {

    val model: Value<Model>

    fun onEmojiClicked(emoji: CustomEmoji)

    data class Model(
        val emojiPickerAvailable: Boolean,
        val emojis: List<CustomEmoji>,
    )
}
