package com.sedsoftware.tackle.compose.platform

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
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.vanniktech.blurhash.BlurHash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import org.jetbrains.skia.Image as SkiaImage

@Composable
actual fun BlurHashView(blurhash: String, width: Int, height: Int, modifier: Modifier) {

    var bitmap: ImageBitmap? by remember { mutableStateOf(null) }

    LaunchedEffect(blurhash) {
        val decoded: BufferedImage? = withContext(Dispatchers.IO) {
            BlurHash.decode(
                blurHash = blurhash,
                width = width / IMAGE_SIDE_DIVIDER,
                height = height / IMAGE_SIDE_DIVIDER,
            )
        }

        withContext(Dispatchers.IO) {
            val stream = ByteArrayOutputStream()
            ImageIO.write(decoded, "png", stream)

            val byteArray = stream.toByteArray()
            bitmap = SkiaImage.makeFromEncoded(byteArray).toComposeImageBitmap()
        }
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
