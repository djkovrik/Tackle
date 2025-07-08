package com.sedsoftware.tackle.compose.ui.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sedsoftware.tackle.compose.extension.clickableOnce
import com.sedsoftware.tackle.main.viewvideo.ViewVideoComponent

@Composable
internal fun ViewVideoContent(
    component: ViewVideoComponent,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "View video",
            modifier = Modifier.clickableOnce(onClick = component::onBack)
        )
    }
}
