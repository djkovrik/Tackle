package com.sedsoftware.tackle.compose.extension

import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.utils.extension.isAudio
import com.sedsoftware.tackle.utils.extension.isImage
import com.sedsoftware.tackle.utils.extension.isVideo
import org.jetbrains.compose.resources.StringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_attachment_audio
import tackle.shared.compose.generated.resources.editor_attachment_image
import tackle.shared.compose.generated.resources.editor_attachment_unknown
import tackle.shared.compose.generated.resources.editor_attachment_video

internal fun AttachedFile.getTypeTitle(): StringResource = when {
    file.isAudio -> Res.string.editor_attachment_audio
    file.isImage -> Res.string.editor_attachment_image
    file.isVideo -> Res.string.editor_attachment_video
    else -> Res.string.editor_attachment_unknown
}

internal val AttachedFile.hasError: Boolean
    get() = status == AttachedFile.Status.ERROR
