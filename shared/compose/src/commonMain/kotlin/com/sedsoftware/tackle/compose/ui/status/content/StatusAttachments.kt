package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.Status

@Composable
internal fun StatusAttachments(
    attachments: List<MediaAttachment>,
    modifier: Modifier = Modifier,
    onDownloadClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onDoneClick: () -> Unit = {},
) {


}
