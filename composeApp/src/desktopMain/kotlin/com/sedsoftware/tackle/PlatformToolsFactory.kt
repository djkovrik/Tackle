package com.sedsoftware.tackle

import com.sedsoftware.tackle.utils.TacklePlatformTools
import com.sedsoftware.tackle.utils.model.AppClientData
import kotlinx.coroutines.flow.StateFlow
import java.awt.Desktop
import java.net.URI

@Suppress("FunctionName")
fun PlatformToolsFactory(): TacklePlatformTools =
    object : TacklePlatformTools {
        override fun openUrl(url: String?) {
            val uri = url?.let { URI.create(it) } ?: return
            Desktop.getDesktop().browse(uri)
        }

        override fun openAuthWebsite(domain: String, clientId: String) {
            TODO("Not yet implemented")
        }

        override fun handleOAuthResponse(code: String?, error: String?, description: String?) {
            TODO("Not yet implemented")
        }

        override fun getOAuthResponseFlow(): StateFlow<Result<String>> {
            TODO("Not yet implemented")
        }

        override fun getClientData(): AppClientData {
            return AppClientData(
                name = "Tackle",
                uri = "tackle://sedsoftware.com",
                scopes = "read write push",
                website = "https://sedsoftware.com/"
            )
        }
    }
