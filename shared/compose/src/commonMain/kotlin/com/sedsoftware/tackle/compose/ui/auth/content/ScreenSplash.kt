package com.sedsoftware.tackle.compose.ui.auth.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sedsoftware.tackle.compose.custom.WanderingCubes

@Composable
internal fun ScreenSplash(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        WanderingCubes(
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier,
        )
    }
}
