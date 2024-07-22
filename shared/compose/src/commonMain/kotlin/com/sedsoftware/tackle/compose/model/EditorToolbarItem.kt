package com.sedsoftware.tackle.compose.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_alert
import tackle.shared.compose.generated.resources.editor_attachment
import tackle.shared.compose.generated.resources.editor_chart
import tackle.shared.compose.generated.resources.editor_content_description_attach
import tackle.shared.compose.generated.resources.editor_content_description_emoji
import tackle.shared.compose.generated.resources.editor_content_description_poll
import tackle.shared.compose.generated.resources.editor_content_description_schedule
import tackle.shared.compose.generated.resources.editor_content_description_warning
import tackle.shared.compose.generated.resources.editor_emoji
import tackle.shared.compose.generated.resources.editor_scheduled

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
