package com.sedsoftware.tackle

import com.sedsoftware.tackle.domain.TacklePlatformTools
import com.sedsoftware.tackle.domain.model.AppClientData
import java.awt.Desktop
import java.net.URI

@Suppress("FunctionName")
fun PlatformToolsFactory(): TacklePlatformTools =
    object : TacklePlatformTools {
        override fun openUrl(url: String?) {
            val uri = url?.let { URI.create(it) } ?: return
            Desktop.getDesktop().browse(uri)
        }

        override fun getClientData(): AppClientData {
            return AppClientData(
                name = "Tackle",
                uri = "http://localhost:8080/redirect",
                scopes = "read write push",
                website = "https://sedsoftware.com/"
            )
        }
    }
