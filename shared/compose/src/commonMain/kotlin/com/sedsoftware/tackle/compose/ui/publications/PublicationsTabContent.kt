package com.sedsoftware.tackle.compose.ui.publications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sedsoftware.tackle.publications.PublicationsTabComponent

@Suppress("UnusedParameter")
@Composable
internal fun PublicationsTabContent(
    component: PublicationsTabComponent,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Publications tab",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
