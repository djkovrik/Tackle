package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sedsoftware.tackle.compose.ui.status.content.media.StatusAttachmentAudio
import com.sedsoftware.tackle.compose.ui.status.content.media.StatusAttachmentSingleImage
import com.sedsoftware.tackle.compose.ui.status.content.media.StatusAttachmentImageGallery
import com.sedsoftware.tackle.compose.ui.status.content.media.StatusAttachmentUnknownFiles
import com.sedsoftware.tackle.compose.ui.status.content.media.StatusAttachmentVideo
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType

@Composable
internal fun StatusAttachment(
    attachments: List<MediaAttachment>,
    hasSensitiveContent: Boolean,
    onContentClick: () -> Unit,
    onContentAltClick: (String) -> Unit,
    onDownloadClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    require(attachments.isNotEmpty()) { "Attachments list should not be empty" }

    val attachmentsType: MediaAttachmentType = remember { attachments.first().type }
    var hideSensitiveContent: Boolean by remember { mutableStateOf(hasSensitiveContent || false) }
    var displayedAttachment: MediaAttachment by remember { mutableStateOf(attachments.first()) }

    when {
        attachmentsType == MediaAttachmentType.AUDIO ->
            StatusAttachmentAudio(
                displayedAttachment = displayedAttachment,
                modifier = modifier,
            )

        attachmentsType == MediaAttachmentType.IMAGE && attachments.size != 1 ->
            StatusAttachmentImageGallery(
                attachments = attachments,
                displayedAttachment = displayedAttachment,
                hasSensitiveContent = hasSensitiveContent,
                hideSensitiveContent = hideSensitiveContent,
                onImageClick = onContentClick,
                onImageAltClick = onContentAltClick,
                onSensitiveClick = { hideSensitiveContent = !hideSensitiveContent },
                onGalleryItemSelect = { displayedAttachment = attachments[it] },
                modifier = modifier,
            )

        attachmentsType == MediaAttachmentType.IMAGE && attachments.size == 1 ->
            StatusAttachmentSingleImage(
                displayedAttachment = displayedAttachment,
                hasSensitiveContent = hasSensitiveContent,
                hideSensitiveContent = hideSensitiveContent,
                onImageClick = onContentClick,
                onImageAltClick = onContentAltClick,
                onSensitiveClick = { hideSensitiveContent = !hideSensitiveContent },
                modifier = modifier,
            )

        attachmentsType == MediaAttachmentType.GIF ||
                attachmentsType == MediaAttachmentType.GIFV ||
                attachmentsType == MediaAttachmentType.VIDEO ->
            StatusAttachmentVideo(
                displayedAttachment = displayedAttachment,
                hasSensitiveContent = hasSensitiveContent,
                hideSensitiveContent = hideSensitiveContent,
                onVideoClick = onContentClick,
                onVideoAltClick = onContentAltClick,
                onSensitiveClick = { hideSensitiveContent = !hideSensitiveContent },
                modifier = modifier,
            )

        attachmentsType == MediaAttachmentType.UNKNOWN ->
            StatusAttachmentUnknownFiles(
                attachments = attachments,
                onFileClick = onContentClick,
                onDownloadClick = onDownloadClick,
                onCancelClick = onCancelClick,
                modifier = modifier,
            )
    }
}
