package com.sedsoftware.tackle.compose.ui.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sedsoftware.tackle.compose.extension.clickableOnce
import com.sedsoftware.tackle.main.viewimage.ViewImageComponent

@Composable
internal fun ViewImageContent(
    component: ViewImageComponent,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "View image",
            modifier = Modifier.clickableOnce(onClick = component::onBack)
        )
    }
}
