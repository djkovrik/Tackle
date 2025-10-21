package com.sedsoftware.tackle.compose.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.Image

actual suspend fun ByteArray.toImageBitmap(): ImageBitmap {
    return withContext(Dispatchers.Unconfined) {
        Image
            .makeFromEncoded(this@toImageBitmap)
            .toComposeImageBitmap()
    }
}
