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
import com.github.panpf.sketch.AsyncImageState
import com.github.panpf.sketch.PainterState
import com.github.panpf.sketch.SubcomposeAsyncImage
import com.github.panpf.sketch.rememberAsyncImageState
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
    val state: AsyncImageState = rememberAsyncImageState()

    SubcomposeAsyncImage(
        uri = imageUrl,
        state = state,
        contentDescription = contentDescription,
        modifier = modifier.aspectRatio(ratio = imageRatio),
        content = {
            when (state.painterState) {
                is PainterState.Loading -> {
                    TackleImageLoading(
                        blurhash = imageParams?.blurhash.orEmpty(),
                        modifier = modifier.aspectRatio(ratio = imageRatio),
                    )
                }

                is PainterState.Error -> {
                    TackleImageError(
                        modifier = modifier.aspectRatio(ratio = imageRatio),
                    )
                }

                else -> {
                    Image(
                        painter = painter,
                        contentDescription = contentDescription,
                        modifier = modifier
                            .aspectRatio(ratio = imageRatio)
                            .fillMaxSize(),
                    )
                }
            }
        },
        alignment = alignment,
        alpha = alpha,
        contentScale = contentScale,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
    )
}
