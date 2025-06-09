package com.sedsoftware.tackle.editor.emojis.integration

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent.Model

class EditorEmojisComponentPreview(
    emojis: List<CustomEmoji> = emptyList(),
    emojisButtonAvailable: Boolean = false,
    emojisContentVisible: Boolean = false,
) : EditorEmojisComponent {

    override val model: Value<Model> =
        MutableValue(
            Model(
                emojis = emojis.groupBy { it.category },
                emojisButtonAvailable = emojisButtonAvailable,
                emojisContentVisible = emojisContentVisible,
            )
        )

    override fun onEmojiClick(emoji: CustomEmoji) = Unit
    override fun toggleComponentVisibility() = Unit
}
