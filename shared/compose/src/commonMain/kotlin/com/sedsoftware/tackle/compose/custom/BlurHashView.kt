package com.sedsoftware.tackle.compose.custom

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brys.compose.blurhash.BlurHashImage
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun BlurHashView(
    blurhash: String,
    ratio: Float,
    modifier: Modifier = Modifier,
) {
    BlurHashImage(
        hash = blurhash,
        contentDescription = "",
        modifier = modifier
            .aspectRatio(ratio = ratio)
            .fillMaxSize()
    )
}

@Preview
@Composable
private fun BlurHashPlaceholderPreview() {
    TackleScreenPreview {
        BlurHashView(
            blurhash = "UJOoqX\$P*|oz}@%gELX9+sIW9vrr?GZhxYVs",
            ratio = 1.5f,
        )
    }
}
