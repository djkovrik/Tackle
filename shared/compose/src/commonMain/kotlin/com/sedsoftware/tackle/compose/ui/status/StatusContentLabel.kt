package com.sedsoftware.tackle.compose.ui.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun StatusContentLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.inverseSurface.copy(
                        alpha = 0.75f
                    ),
                    shape = MaterialTheme.shapes.extraSmall,
                )
                .padding(all = 4.dp),
        )
    }
}
