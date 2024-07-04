package com.sedsoftware.tackle.compose.model

import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_status_visibility_direct
import tackle.shared.compose.generated.resources.editor_status_visibility_direct_desc
import tackle.shared.compose.generated.resources.editor_status_visibility_private
import tackle.shared.compose.generated.resources.editor_status_visibility_private_desc
import tackle.shared.compose.generated.resources.editor_status_visibility_public
import tackle.shared.compose.generated.resources.editor_status_visibility_public_desc
import tackle.shared.compose.generated.resources.editor_status_visibility_unlisted
import tackle.shared.compose.generated.resources.editor_status_visibility_unlisted_desc
import tackle.shared.compose.generated.resources.visibility_direct
import tackle.shared.compose.generated.resources.visibility_private
import tackle.shared.compose.generated.resources.visibility_public
import tackle.shared.compose.generated.resources.visibility_unlisted

internal fun StatusVisibility.getIcon(): DrawableResource = when (this) {
    StatusVisibility.PUBLIC -> Res.drawable.visibility_public
    StatusVisibility.UNLISTED -> Res.drawable.visibility_unlisted
    StatusVisibility.PRIVATE -> Res.drawable.visibility_private
    StatusVisibility.DIRECT -> Res.drawable.visibility_direct
    StatusVisibility.UNKNOWN -> Res.drawable.visibility_public
}

internal fun StatusVisibility.getTitle(): StringResource = when (this) {
    StatusVisibility.PUBLIC -> Res.string.editor_status_visibility_public
    StatusVisibility.UNLISTED -> Res.string.editor_status_visibility_unlisted
    StatusVisibility.PRIVATE -> Res.string.editor_status_visibility_private
    StatusVisibility.DIRECT -> Res.string.editor_status_visibility_direct
    StatusVisibility.UNKNOWN -> Res.string.editor_status_visibility_public
}

internal fun StatusVisibility.getDescription(): StringResource = when (this) {
    StatusVisibility.PUBLIC -> Res.string.editor_status_visibility_public_desc
    StatusVisibility.UNLISTED -> Res.string.editor_status_visibility_unlisted_desc
    StatusVisibility.PRIVATE -> Res.string.editor_status_visibility_private_desc
    StatusVisibility.DIRECT -> Res.string.editor_status_visibility_direct_desc
    StatusVisibility.UNKNOWN -> Res.string.editor_status_visibility_public_desc
}
