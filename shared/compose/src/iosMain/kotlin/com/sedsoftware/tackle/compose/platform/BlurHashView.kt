package com.sedsoftware.tackle.compose.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import com.sedsoftware.tackle.compose.ComposeGlobals
import com.vanniktech.blurhash.BlurHash
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIImage

@Composable
@OptIn(ExperimentalForeignApi::class)
actual fun BlurHashView(blurhash: String, width: Int, height: Int, modifier: Modifier) {

    var uiImage: UIImage? by remember { mutableStateOf(null) }

    LaunchedEffect(blurhash) {

        val decoded: UIImage? = BlurHash.decode(
            blurHash = blurhash,
            width = width.toDouble(),
            height = height.toDouble(),
            punch = 1.0f,
            useCache = true,
        )

        uiImage = decoded
    }

    uiImage?.let {
        UIKitViewController(
            factory = { ComposeGlobals.controllerFactory!!(it) },
            modifier = modifier,
        )
    }
}
