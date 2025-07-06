package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sedsoftware.tackle.compose.ui.status.content.media.StatusAttachmentAudio
import com.sedsoftware.tackle.compose.ui.status.content.media.StatusAttachmentImage
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
    onContentAltClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    require(attachments.isNotEmpty()) { "Attachments list should not be empty" }

    val baseType: MediaAttachmentType = attachments.first().type
    var hideSensitiveContent: Boolean by remember { mutableStateOf(hasSensitiveContent || false) }

    when {
        baseType == MediaAttachmentType.AUDIO ->
            StatusAttachmentAudio(
                attachment = attachments.first(),
                modifier = modifier,
            )

        baseType == MediaAttachmentType.IMAGE && attachments.size == 1 ->
            StatusAttachmentImage(
                attachment = attachments.first(),
                hasSensitiveContent = hasSensitiveContent,
                hideSensitiveContent = hideSensitiveContent,
                onImageClick = onContentClick,
                onImageAltClick = onContentAltClick,
                onSensitiveClick = { hideSensitiveContent = !hideSensitiveContent },
                modifier = modifier,
            )

        baseType == MediaAttachmentType.IMAGE ->
            StatusAttachmentImageGallery(
                attachments = attachments,
                hasSensitiveContent = hasSensitiveContent,
                hideSensitiveContent = hideSensitiveContent,
                onImageClick = onContentClick,
                onImageAltClick = onContentAltClick,
                onSensitiveClick = { hideSensitiveContent = !hideSensitiveContent },
                modifier = modifier,
            )

        baseType == MediaAttachmentType.GIF ||
                baseType == MediaAttachmentType.GIFV ||
                baseType == MediaAttachmentType.VIDEO ->
            StatusAttachmentVideo(
                attachment = attachments.first(),
                hasSensitiveContent = hasSensitiveContent,
                hideSensitiveContent = hideSensitiveContent,
                onVideoClick = onContentClick,
                onVideoAltClick = onContentAltClick,
                onSensitiveClick = { hideSensitiveContent = !hideSensitiveContent },
                modifier = modifier,
            )

        baseType == MediaAttachmentType.UNKNOWN ->
            StatusAttachmentUnknownFiles(
                attachments = attachments,
                onFileClick = onContentClick,
                onDownloadClick = onDownloadClick,
                onCancelClick = onCancelClick,
                modifier = modifier,
            )
    }
}
