package com.sedsoftware.tackle.compose.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brys.compose.blurhash.BlurHashImage
import com.sedsoftware.tackle.compose.custom.ShimmerEffectBrush

@Composable
internal fun TackleImageLoading(
    blurhash: String,
    modifier: Modifier = Modifier,
) {
    if (blurhash.isNotEmpty()) {
        BlurHashImage(
            hash = blurhash,
            contentDescription = "",
            modifier = modifier.fillMaxSize()
        )
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(brush = ShimmerEffectBrush()),
        )
    }
}
