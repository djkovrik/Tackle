package com.sedsoftware.tackle.compose.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.brys.compose.blurhash.BlurHashImage
import com.github.panpf.sketch.PainterState
import com.github.panpf.sketch.SubcomposeAsyncImage
import com.sedsoftware.tackle.compose.custom.ShimmerEffectBrush
import com.sedsoftware.tackle.compose.extension.alsoIf
import com.sedsoftware.tackle.compose.model.TackleImageParams
import org.jetbrains.compose.resources.painterResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_attachment_broken

@Composable
internal fun TackleImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    sensitive: Boolean = false,
    contentScale: ContentScale = ContentScale.Fit,
    params: TackleImageParams? = null,
) {
    val imageRatio: Float = if (params?.ratio != null && params.ratio > 0f) params.ratio else 1f

    SubcomposeAsyncImage(
        uri = imageUrl,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier.aspectRatio(ratio = imageRatio),
        content = {
            when {
                state.painterState is PainterState.Loading || sensitive -> {
                    LoadingPlaceholder(
                        params = params,
                        modifier = modifier,
                    )
                }

                state.painterState is PainterState.Error -> {
                    ErrorPlaceholder(
                        params = params,
                        modifier = modifier,
                    )
                }

                else -> {
                    Image(
                        painter = painter,
                        contentDescription = contentDescription,
                        modifier = modifier.fillMaxSize(),
                    )
                }
            }
        }
    )
}

@Composable
private fun LoadingPlaceholder(
    params: TackleImageParams?,
    modifier: Modifier = Modifier,
) {
    if (!params?.blurhash.isNullOrEmpty()) {
        BlurHashImage(
            hash = params.blurhash,
            contentDescription = "",
            modifier = modifier
                .alsoIf(
                    condition = params.ratio > 0f,
                    other = Modifier.aspectRatio(ratio = params.ratio.takeIf { it > 0f } ?: 1f)
                )
                .fillMaxSize()
        )
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(brush = ShimmerEffectBrush())
                .alsoIf(
                    condition = params?.ratio != null && params.ratio > 0f,
                    other = Modifier.aspectRatio(ratio = params?.ratio ?: 1f)
                ),
        )
    }
}

@Composable
private fun ErrorPlaceholder(
    params: TackleImageParams?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.errorContainer)
            .alsoIf(
                condition = params?.ratio != null && params.ratio > 0f,
                other = Modifier.aspectRatio(ratio = params?.ratio ?: 1f)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(resource = Res.drawable.editor_attachment_broken),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(size = 24.dp),
        )
    }
}
