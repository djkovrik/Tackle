package com.sedsoftware.tackle

import com.sedsoftware.tackle.domain.TacklePlatformTools
import com.sedsoftware.tackle.domain.model.AppClientData
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Suppress("FunctionName")
fun PlatformToolsFactory(): TacklePlatformTools =
    object : TacklePlatformTools {
        override fun openUrl(url: String?) {
            val nsUrl = url?.let { NSURL.URLWithString(it) } ?: return
            UIApplication.sharedApplication.openURL(nsUrl)
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
