package com.sedsoftware.tackle.compose.extension

import com.sedsoftware.tackle.main.model.StatusContextAction
import org.jetbrains.compose.resources.StringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.status_menu_bookmark
import tackle.shared.compose.generated.resources.status_menu_delete
import tackle.shared.compose.generated.resources.status_menu_mute
import tackle.shared.compose.generated.resources.status_menu_pin
import tackle.shared.compose.generated.resources.status_menu_show_original
import tackle.shared.compose.generated.resources.status_menu_translate
import tackle.shared.compose.generated.resources.status_menu_unbookmark
import tackle.shared.compose.generated.resources.status_menu_unmute
import tackle.shared.compose.generated.resources.status_menu_unpin

internal fun StatusContextAction.getTitle(): StringResource = when (this) {
    StatusContextAction.TRANSLATE -> Res.string.status_menu_translate
    StatusContextAction.SHOW_ORIGINAL -> Res.string.status_menu_show_original
    StatusContextAction.DELETE -> Res.string.status_menu_delete
    StatusContextAction.PIN -> Res.string.status_menu_pin
    StatusContextAction.UNPIN -> Res.string.status_menu_unpin
    StatusContextAction.BOOKMARK -> Res.string.status_menu_bookmark
    StatusContextAction.UNBOOKMARK -> Res.string.status_menu_unbookmark
    StatusContextAction.MUTE -> Res.string.status_menu_mute
    StatusContextAction.UNMUTE -> Res.string.status_menu_unmute
}
