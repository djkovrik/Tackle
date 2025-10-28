package com.sedsoftware.tackle.compose.ui.statuslist.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StatusLoadMoreIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(width = 16.dp),
            color = color,
            trackColor = trackColor,
            strokeWidth = 2.dp,
        )
    }
}

@Preview
@Composable
private fun StatusLoadMoreIndicatorPreview() {
    TackleScreenPreview {
        StatusLoadMoreIndicator()
    }
}
