package com.sedsoftware.tackle.compose.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sedsoftware.tackle.home.HomeTabComponent

@Suppress("UnusedParameter")
@Composable
internal fun HomeTabContent(
    component: HomeTabComponent,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Home tab",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
