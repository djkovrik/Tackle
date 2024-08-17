package com.sedsoftware.tackle.compose.custom

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.sedsoftware.tackle.compose.platform.BlurHashView
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview

@Composable
internal fun BlurHashPlaceholder(
    blurhash: String,
    width: Int,
    height: Int,
    modifier: Modifier = Modifier,
) {
    val ratio: Float = remember { width.toFloat() / height.toFloat() }

    BlurHashView(
        blurhash = blurhash,
        width = width,
        height = height,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(ratio = ratio)
    )
}

@Preview
@Composable
private fun BlurHashPlaceholderPreview() {
    TackleScreenPreview {
        BlurHashPlaceholder(
            blurhash = "UJOoqX\$P*|oz}@%gELX9+sIW9vrr?GZhxYVs",
            width = 588,
            height = 392,
        )
    }
}
