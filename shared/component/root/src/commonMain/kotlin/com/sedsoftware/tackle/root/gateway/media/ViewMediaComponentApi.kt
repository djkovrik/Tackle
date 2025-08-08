package com.sedsoftware.tackle.root.gateway.media

import com.sedsoftware.tackle.domain.api.UnauthorizedApi
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponentGateways

internal class ViewMediaComponentApi(
    private val api: UnauthorizedApi,
) : ViewMediaComponentGateways.Api {

    override suspend fun downloadFile(url: String, onDone: suspend (ByteArray) -> Unit) = api.downloadFile(url, onDone)
}
