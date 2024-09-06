package com.sedsoftware.tackle.editor.details.domain

import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsGateways
import com.sedsoftware.tackle.utils.extension.roundToDecimals

class EditorAttachmentDetailsManager(
    private val api: EditorAttachmentDetailsGateways.Api,
) {

    suspend fun updateFile(id: String, description: String, focus: Pair<Float, Float>): Result<MediaAttachment> = runCatching {
        val x = focus.first.roundToDecimals(1)
        val y = focus.second.roundToDecimals(1)
        val focusStr = "$x, $y"

        api.updateFile(
            id = id,
            description = description.takeIf { it.isNotEmpty() },
            focus = focusStr.takeIf { focus != 0f to 0f },
        )
    }
}
