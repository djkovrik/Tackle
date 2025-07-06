package com.sedsoftware.tackle.compose.ui.status.content.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.widget.TackleProgressIndicator
import com.sedsoftware.tackle.domain.model.MediaAttachment
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_attachment_unknown

@Composable
internal fun StatusAttachmentUnknownFiles(
    attachments: List<MediaAttachment>,
    onFileClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(modifier = modifier) {
        itemsIndexed(
            items = attachments,
            key = { index: Int, _ -> index }
        ) { index: Int, item: MediaAttachment ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.extraSmall)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .padding(start = 8.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape
                        )
                        .size(size = 32.dp)
                ) {
                    TackleProgressIndicator(
                        progress = 0f,
                        size = 32.dp,
                        indicatorColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.surface,
                        onDownloadClick = onDownloadClick,
                        onCancelClick = onCancelClick,
                        onDoneClick = onFileClick,
                    )
                }
                Text(
                    text = "${stringResource(resource = Res.string.editor_attachment_unknown)} #$index",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier.padding(horizontal = 8.dp, vertical = 16.dp),
                )
            }

            Spacer(modifier = Modifier.width(width = 4.dp))
        }
    }
}
