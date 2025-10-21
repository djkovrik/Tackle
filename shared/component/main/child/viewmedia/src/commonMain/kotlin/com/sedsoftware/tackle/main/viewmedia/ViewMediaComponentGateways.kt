package com.sedsoftware.tackle.main.viewmedia

import kotlinx.coroutines.flow.Flow

interface ViewMediaComponentGateways {
    interface Api {
        suspend fun downloadFile(url: String, onDone: suspend (ByteArray) -> Unit): Flow<Float>
    }
}
