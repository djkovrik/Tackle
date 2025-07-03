package com.sedsoftware.tackle.compose.widget

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.custom.BlurHashView
import com.sedsoftware.tackle.compose.custom.ShimmerEffectBrush
import com.sedsoftware.tackle.compose.extension.alsoIf
import com.sedsoftware.tackle.compose.model.TackleImageParams
import com.sedsoftware.tackle.compose.model.TackleImageState
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import com.seiko.imageloader.rememberImagePainter
import org.jetbrains.compose.resources.painterResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_attachment_broken

@Composable
internal fun TackleImage(
    data: Any,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    errorModifier: Modifier = modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    contentAlignment: Alignment = Alignment.Center,
    animationSpec: FiniteAnimationSpec<Float>? = tween(),
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    params: TackleImageParams? = null,
    sensitive: Boolean = false,
    onLoading: (@Composable BoxScope.() -> Unit) = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = ShimmerEffectBrush())
                .alsoIf(
                    condition = params?.ratio != null,
                    other = Modifier.aspectRatio(ratio = params?.ratio ?: 1f)
                ),
        )
    },
    onFailure: (@Composable BoxScope.(Throwable) -> Unit) = {
        Box(
            modifier = errorModifier then Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.errorContainer)
                .alsoIf(
                    condition = params?.ratio != null,
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
    },
) {
    key(data) {
        val request: ImageRequest = remember { ImageRequest(data = data) }
        if (animationSpec != null) {
            val imageAction: ImageAction by rememberImageAction(request = request)
            val error: MutableState<Throwable?> = remember { mutableStateOf(null) }

            val state by derivedStateOf {
                val action = imageAction
                when {
                    action is ImageAction.Failure -> {
                        error.value = action.error
                        TackleImageState.FAILURE
                    }

                    action is ImageAction.Loading && !params?.blurhash.isNullOrEmpty() -> {
                        TackleImageState.LOADING_BLURHASH
                    }

                    action is ImageAction.Success && sensitive && !params?.blurhash.isNullOrEmpty() -> {
                        TackleImageState.SUCCESS_SENSITIVE
                    }

                    action is ImageAction.Success && !sensitive -> {
                        TackleImageState.SUCCESS
                    }

                    else -> {
                        TackleImageState.LOADING
                    }
                }
            }

            val imageRatio: Float = if (params?.ratio != null && params.ratio > 0f) params.ratio else 1f

            Crossfade(
                targetState = state,
                animationSpec = animationSpec,
                modifier = modifier.aspectRatio(ratio = imageRatio),
            ) { imageState: TackleImageState ->
                Box(
                    contentAlignment = contentAlignment,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    when (imageState) {
                        TackleImageState.SUCCESS ->
                            Image(
                                painter = rememberImageActionPainter(
                                    action = imageAction,
                                    filterQuality = filterQuality,
                                ),
                                contentDescription = contentDescription,
                                modifier = Modifier.fillMaxSize(),
                                alignment = alignment,
                                contentScale = contentScale,
                                alpha = alpha,
                                colorFilter = colorFilter,
                            )

                        TackleImageState.LOADING ->
                            onLoading()

                        TackleImageState.SUCCESS_SENSITIVE,
                        TackleImageState.LOADING_BLURHASH,
                            ->
                            BlurHashView(
                                blurhash = params!!.blurhash,
                                width = params.width,
                                height = params.height,
                            )

                        TackleImageState.FAILURE ->
                            onFailure(error.value ?: return@Crossfade)
                    }
                }
            }
        } else {
            Box(
                contentAlignment = contentAlignment,
                modifier = modifier.alsoIf(
                    condition = params?.ratio != null,
                    other = Modifier.aspectRatio(ratio = params?.ratio ?: 1f)
                ),
            ) {
                Image(
                    painter = rememberImagePainter(
                        request = request,
                        filterQuality = filterQuality
                    ),
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize(),
                    alignment = alignment,
                    contentScale = contentScale,
                    alpha = alpha,
                    colorFilter = colorFilter,
                )
            }
        }
    }
}
