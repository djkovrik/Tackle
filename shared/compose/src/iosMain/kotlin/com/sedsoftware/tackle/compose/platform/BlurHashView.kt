package com.sedsoftware.tackle.compose.platform

import androidx.compose.foundation.layout.fillMaxSize
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
import platform.UIKit.UIImage

@Composable
actual fun BlurHashView(blurhash: String, width: Int, height: Int, modifier: Modifier) {

    var uiImage: UIImage? by remember { mutableStateOf(null) }

    LaunchedEffect(blurhash) {

        val decoded: UIImage? = BlurHash.decode(
            blurHash = blurhash,
            width = (width / IMAGE_SIDE_DIVIDER).toDouble(),
            height = (height / IMAGE_SIDE_DIVIDER).toDouble(),
            punch = 1.0f,
            useCache = true,
        )

        uiImage = decoded
    }

    uiImage?.let {
        UIKitViewController(
            factory = { ComposeGlobals.controllerFactory!!(it) },
            modifier = modifier.fillMaxSize(),
        )
    }
}

private const val IMAGE_SIDE_DIVIDER = 4
