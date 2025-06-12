package com.sedsoftware.tackle.editor.emojis

import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.CustomEmoji

interface EditorEmojisComponent {

    val model: Value<Model>

    fun onEmojiClick(emoji: CustomEmoji)
    fun toggleComponentVisibility()

    data class Model(
        val emojis: Map<String, List<CustomEmoji>>,
        val emojisButtonAvailable: Boolean,
        val emojisContentVisible: Boolean,
    )
}
