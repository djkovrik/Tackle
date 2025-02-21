package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType

@Composable
internal fun StatusAttachments(
    attachments: List<MediaAttachment>,
    modifier: Modifier = Modifier,
    onDownloadClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
) {
    require(attachments.isNotEmpty()) { "Attachments list should not be empty" }

    val baseType: MediaAttachmentType = attachments.first().type

    if (baseType == MediaAttachmentType.AUDIO || baseType == MediaAttachmentType.UNKNOWN) {
        StatusAttachmentsFileList(
            attachments = attachments,
            modifier = modifier,
            onDownloadClick = onDownloadClick,
            onCancelClick = onCancelClick,
            onDoneClick = onDoneClick
        )
    } else {
        StatusAttachmentsImageList(
            attachments = attachments,
            modifier = modifier,
        )
    }
}
