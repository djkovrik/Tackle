package com.sedsoftware.tackle.compose.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import com.github.panpf.sketch.SubcomposeAsyncImage
import com.sedsoftware.tackle.compose.model.TackleImageParams

@Composable
internal fun TackleImage(
    imageUrl: String,
    imageParams: TackleImageParams?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
) {
    val imageRatio: Float = remember { imageParams.takeIf { it != null }?.ratio ?: 1f }

    SubcomposeAsyncImage(
        uri = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier.aspectRatio(ratio = imageRatio),
        loading = {
            TackleImageLoading(
                blurhash = imageParams?.blurhash.orEmpty(),
                modifier = modifier.aspectRatio(ratio = imageRatio),
            )
        },
        error = {
            TackleImageError(
                modifier = modifier.aspectRatio(ratio = imageRatio),
            )
        },
        success = {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier
                    .aspectRatio(ratio = imageRatio)
                    .fillMaxSize(),
            )
        },
        alignment = alignment,
        alpha = alpha,
        contentScale = contentScale,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
    )
}
