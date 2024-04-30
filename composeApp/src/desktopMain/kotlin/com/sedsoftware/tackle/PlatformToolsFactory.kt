package com.sedsoftware.tackle

import com.sedsoftware.tackle.utils.TacklePlatformTools
import java.awt.Desktop
import java.net.URI

@Suppress("FunctionName")
fun PlatformToolsFactory(): TacklePlatformTools =
    object : TacklePlatformTools {
        override fun openUrl(url: String?) {
            val uri = url?.let { URI.create(it) } ?: return
            Desktop.getDesktop().browse(uri)
        }
    }
