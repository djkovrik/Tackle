package com.sedsoftware.tackle.compose.model

internal data class EditorToolbarItem(
    val type: Type,
    val active: Boolean,
    val enabled: Boolean,
) {
    enum class Type {
        ATTACH,
        EMOJIS,
        POLL,
        WARNING,
        SCHEDULE;
    }
}
