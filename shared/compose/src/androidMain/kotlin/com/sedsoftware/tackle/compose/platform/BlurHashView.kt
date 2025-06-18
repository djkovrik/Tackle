package com.sedsoftware.tackle.compose.platform

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.vanniktech.blurhash.BlurHash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
actual fun BlurHashView(blurhash: String, width: Int, height: Int, modifier: Modifier) {

    var bitmap: ImageBitmap? by remember { mutableStateOf(null) }

    LaunchedEffect(blurhash) {
        val decoded: Bitmap? = withContext(Dispatchers.IO) {
            BlurHash.decode(
                blurHash = blurhash,
                width = width / IMAGE_SIDE_DIVIDER,
                height = height / IMAGE_SIDE_DIVIDER,
            )
        }
        bitmap = decoded?.asImageBitmap()
    }

    bitmap?.let {
        Image(
            bitmap = it,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier.fillMaxSize(),
        )
    }
}

private const val IMAGE_SIDE_DIVIDER = 4
