package com.sedsoftware.tackle.compose.ui.status.content

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.compose.widget.TackleImage

@Composable
private fun StatusAttachmentGalleryPreviewItem(
    url: String,
    selected: Boolean,
    params: TackleImageParams? = null,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = 8.dp))
            .border(
                width = 1.dp,
                color = if (selected) {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.1f)
                },
                shape = RoundedCornerShape(size = 8.dp)
            )
            .clickable(onClick = onSelect)
    ) {
        TackleImage(
            data = url,
            contentDescription = null,
            params = params,
            modifier = Modifier
                .height(height = 64.dp)
                .aspectRatio(ratio = params?.ratio ?: 1f)
        )
    }
}
