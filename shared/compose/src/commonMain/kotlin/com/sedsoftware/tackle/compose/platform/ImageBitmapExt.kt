package com.sedsoftware.tackle.compose.platform

import androidx.compose.ui.graphics.ImageBitmap

expect suspend fun ByteArray.toImageBitmap(): ImageBitmap
