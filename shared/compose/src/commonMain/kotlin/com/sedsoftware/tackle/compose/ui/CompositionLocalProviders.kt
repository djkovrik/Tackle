package com.sedsoftware.tackle.compose.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings

@OptIn(ExperimentalSharedTransitionApi::class)
object CompositionLocalProviders {
    val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
    val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
    val LocalFileKitDialogSettings = compositionLocalOf<FileKitDialogSettings?> { null }
}
