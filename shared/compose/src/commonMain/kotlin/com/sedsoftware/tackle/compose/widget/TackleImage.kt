package com.sedsoftware.tackle.compose.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.brys.compose.blurhash.BlurHashImage
import com.github.panpf.sketch.AsyncImagePainter
import com.github.panpf.sketch.AsyncImageState
import com.github.panpf.sketch.PainterState
import com.github.panpf.sketch.SubcomposeAsyncImage
import com.github.panpf.sketch.rememberAsyncImageState
import com.sedsoftware.tackle.compose.custom.ShimmerEffectBrush
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.utils.extension.orZero
import org.jetbrains.compose.resources.painterResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_attachment_broken

/**
 * Image wrapper with LCE views and loading progress indicator.
 *
 * @param imageUrl Image url to load.
 * @param imageParams Image parameters wrapper containing ration and blurhash
 * @param contentDescription Text used by accessibility services to describe what this image
 *  represents. This should always be provided unless this image is used for decorative purposes,
 *  and does not represent a meaningful action that a user can take.
 * @param modifier Modifier used to adjust the layout algorithm or draw decoration content.
 * @param showProgress If true then image will display loading progress indicator
 * @param progressSize Loading progress indicator size
 * @param alignment Optional alignment parameter used to place the [AsyncImagePainter] in the given
 *  bounds defined by the width and height.
 * @param contentScale Optional scale parameter used to determine the aspect ratio scaling to be
 *  used if the bounds are a different size from the intrinsic size of the [AsyncImagePainter].
 * @param alpha Optional opacity to be applied to the [AsyncImagePainter] when it is rendered
 *  onscreen.
 * @param colorFilter Optional [ColorFilter] to apply for the [AsyncImagePainter] when it is
 *  rendered onscreen.
 * @param filterQuality Sampling algorithm applied to a bitmap when it is scaled and drawn into the
 *  destination.
 */
@Composable
internal fun TackleImage(
    imageUrl: String,
    imageParams: TackleImageParams?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    showProgress: Boolean = false,
    progressSize: Dp = 32.dp,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
) {
    val blurhash: String = remember { imageParams?.blurhash.orEmpty() }
    val imageRatio: Float = remember { imageParams.takeIf { it != null }?.ratio ?: 1f }
    val state: AsyncImageState = rememberAsyncImageState()
    val progress: Float = state.progress?.decimalProgress.orZero()

    SubcomposeAsyncImage(
        uri = imageUrl,
        state = state,
        contentDescription = contentDescription,
        modifier = modifier.aspectRatio(ratio = imageRatio),
        content = {
            when (state.painterState) {
                is PainterState.Loading -> {
                    Box {
                        if (blurhash.isNotEmpty()) {
                            BlurHashImage(
                                hash = blurhash,
                                contentDescription = "",
                                modifier = modifier
                                    .aspectRatio(ratio = imageRatio)
                                    .fillMaxWidth()
                            )
                        } else {
                            Box(
                                modifier = modifier
                                    .aspectRatio(ratio = imageRatio)
                                    .fillMaxWidth()
                                    .background(brush = ShimmerEffectBrush()),
                            )
                        }

                        if (showProgress) {
                            TackleImageProgress(
                                progress = progress,
                                progressSize = progressSize,
                                modifier = Modifier.align(Alignment.Center),
                                indicatorColor = MaterialTheme.colorScheme.inverseOnSurface.copy(
                                    alpha = 0.75f
                                ),
                                containerColor = MaterialTheme.colorScheme.inverseSurface.copy(
                                    alpha = 0.5f
                                ),
                            )
                        }
                    }
                }

                is PainterState.Error -> {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.errorContainer),
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
