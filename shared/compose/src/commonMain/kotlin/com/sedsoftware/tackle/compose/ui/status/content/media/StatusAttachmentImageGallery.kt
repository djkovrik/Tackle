package com.sedsoftware.tackle.compose.ui.status.content.media

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.domain.model.MediaAttachment

@Composable
internal fun StatusAttachmentImageGallery(
    attachments: List<MediaAttachment>,
    displayedAttachment: MediaAttachment,
    hasSensitiveContent: Boolean,
    hideSensitiveContent: Boolean,
    onImageClick: () -> Unit,
    onImageAltClick: (String) -> Unit,
    onSensitiveClick: () -> Unit,
    onGalleryItemSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(modifier = modifier) {
        StatusAttachmentSingleImage(
            displayedAttachment = displayedAttachment,
            hasSensitiveContent = hasSensitiveContent,
            hideSensitiveContent = hideSensitiveContent,
            onImageClick = onImageClick,
            onImageAltClick = onImageAltClick,
            onSensitiveClick = onSensitiveClick,
        )

        LazyRow(modifier = Modifier.padding(all = 4.dp)) {
            itemsIndexed(
                items = attachments,
                key = { index: Int, _ -> index }
            ) { index: Int, item: MediaAttachment ->

                StatusAttachmentImageGalleryItem(
                    galleryItem = item,
                    selected = index == attachments.indexOfFirst { it.id == displayedAttachment.id },
                    onSelect = { onGalleryItemSelect.invoke(index) },
                    sensitive = hideSensitiveContent,
                )

                if (index != attachments.lastIndex) {
                    Spacer(modifier = Modifier.width(width = 4.dp))
                }
            }
        }
    }
}
