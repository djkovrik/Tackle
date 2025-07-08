package com.sedsoftware.tackle.compose.ui.status.content.media

import MediaPlayer
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.domain.model.MediaAttachment

@Composable
internal fun StatusAttachmentAudio(
    displayedAttachment: MediaAttachment,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(height = 132.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.extraSmall,
            )
    ) {
        MediaPlayer(
            modifier = modifier.fillMaxWidth(),
            url = displayedAttachment.url,
            startTime = MaterialTheme.colorScheme.onSurface,
            endTime =  MaterialTheme.colorScheme.onSurface,
            volumeIconColor = MaterialTheme.colorScheme.primary,
            playIconColor = MaterialTheme.colorScheme.primary,
            sliderTrackColor = MaterialTheme.colorScheme.primary,
            sliderIndicatorColor = MaterialTheme.colorScheme.primary,
            showControls = false,
            autoPlay = false,
            headers = emptyMap(),
        )
    }
}
