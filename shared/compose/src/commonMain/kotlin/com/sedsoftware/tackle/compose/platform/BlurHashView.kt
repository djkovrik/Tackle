package com.sedsoftware.tackle.compose.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun BlurHashView(blurhash: String, width: Int, height: Int, modifier: Modifier)
