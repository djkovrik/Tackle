package com.sedsoftware.tackle.compose.custom

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sedsoftware.tackle.compose.platform.BlurHashView
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview

@Composable
internal fun BlurHashView(
    blurhash: String,
    width: Int,
    height: Int,
    modifier: Modifier = Modifier,
) {
    BlurHashView(
        blurhash = blurhash,
        width = width,
        height = height,
        modifier = modifier
    )
}

@Preview
@Composable
private fun BlurHashPlaceholderPreview() {
    TackleScreenPreview {
        BlurHashView(
            blurhash = "UJOoqX\$P*|oz}@%gELX9+sIW9vrr?GZhxYVs",
            width = 588,
            height = 392,
        )
    }
}
