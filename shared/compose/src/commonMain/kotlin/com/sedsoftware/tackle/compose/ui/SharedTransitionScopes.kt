package com.sedsoftware.tackle.compose.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf

@OptIn(ExperimentalSharedTransitionApi::class)
internal object SharedTransitionScopes {
    val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
    val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
}
