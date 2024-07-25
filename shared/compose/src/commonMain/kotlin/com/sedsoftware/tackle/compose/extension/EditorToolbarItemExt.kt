package com.sedsoftware.tackle.compose.extension

import com.sedsoftware.tackle.compose.model.EditorToolbarItem
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

internal fun EditorToolbarItem.Type.getIcon(): DrawableResource = when (this) {
    EditorToolbarItem.Type.ATTACH -> Res.drawable.editor_attachment
    EditorToolbarItem.Type.EMOJIS -> Res.drawable.editor_emoji
    EditorToolbarItem.Type.POLL -> Res.drawable.editor_chart
    EditorToolbarItem.Type.WARNING -> Res.drawable.editor_alert
    EditorToolbarItem.Type.SCHEDULE -> Res.drawable.editor_scheduled
}

internal fun EditorToolbarItem.Type.getContentDescription(): StringResource = when (this) {
    EditorToolbarItem.Type.ATTACH -> Res.string.editor_content_description_attach
    EditorToolbarItem.Type.EMOJIS -> Res.string.editor_content_description_emoji
    EditorToolbarItem.Type.POLL -> Res.string.editor_content_description_poll
    EditorToolbarItem.Type.WARNING -> Res.string.editor_content_description_warning
    EditorToolbarItem.Type.SCHEDULE -> Res.string.editor_content_description_schedule
}
