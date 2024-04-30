package com.sedsoftware.tackle

import com.sedsoftware.tackle.utils.TacklePlatformTools
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Suppress("FunctionName")
fun PlatformToolsFactory(): TacklePlatformTools =
    object : TacklePlatformTools {
        override fun openUrl(url: String?) {
            val nsUrl = url?.let { NSURL.URLWithString(it) } ?: return
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }
