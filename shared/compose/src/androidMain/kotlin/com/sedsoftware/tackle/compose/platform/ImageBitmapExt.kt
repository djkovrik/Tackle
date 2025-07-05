package com.sedsoftware.tackle.compose.platform

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual suspend fun ByteArray.toImageBitmap(): ImageBitmap {
    return withContext(Dispatchers.IO) {
        val bitmap = BitmapFactory.decodeByteArray(this@toImageBitmap, 0, this@toImageBitmap.size)
        bitmap.asImageBitmap()
    }
}
