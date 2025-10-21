package com.sedsoftware.tackle.main.viewmedia.domain

import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponentGateways
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.flow.Flow


internal class FileDownloadManager(
    private val api: ViewMediaComponentGateways.Api,
) {
    suspend fun downloadFile(url: String, destination: PlatformFile): Flow<Float> =
        api.downloadFile(url) { result: ByteArray ->
            destination.write(result)
        }
}
