package com.sedsoftware.tackle.domain.model.type

import com.sedsoftware.tackle.domain.model.CustomEmoji

sealed class StatusInlineContent {
    data class TextPart(val text: String) : StatusInlineContent()
    data class EmojiPart(val text: String, val emoji: CustomEmoji) : StatusInlineContent()
}
