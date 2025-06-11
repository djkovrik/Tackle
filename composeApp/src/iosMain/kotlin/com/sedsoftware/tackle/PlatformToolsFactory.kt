package com.sedsoftware.tackle

import com.sedsoftware.tackle.domain.api.TacklePlatformTools
import com.sedsoftware.tackle.domain.model.AppClientData
import com.sedsoftware.tackle.domain.model.AppLocale
import platform.Foundation.ISOLanguageCodes
import platform.Foundation.NSLocale
import platform.Foundation.NSLocaleLanguageCode
import platform.Foundation.NSURL
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.UIKit.UIApplication
import platform.UIKit.UIActivityViewController

@Suppress("FunctionName")
fun PlatformToolsFactory(): TacklePlatformTools =
    object : TacklePlatformTools {
        private val languageCodeLength = 2

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

        override fun getCurrentLocale(): AppLocale {
            val locale: NSLocale = NSLocale.currentLocale
            return AppLocale(
                languageName = locale.displayNameForKey(NSLocaleLanguageCode, locale.languageCode).orEmpty(),
                languageCode = locale.languageCode,
            )
        }

        override fun getAvailableLocales(): List<AppLocale> {
            val codes = NSLocale.ISOLanguageCodes
            val locale: NSLocale = NSLocale.currentLocale
            return codes
                .filterNotNull()
                .filter { code -> code.toString().length == languageCodeLength }
                .map { code ->
                    AppLocale(
                        languageName = locale.displayNameForKey(NSLocaleLanguageCode, code).orEmpty(),
                        languageCode = code.toString(),
                    )
                }
                .distinctBy { it.languageCode }
        }

        override fun shareStatus(title: String, url: String) {
            val textToShare = "$title\n$url"
            val currentViewController = UIApplication.sharedApplication().keyWindow?.rootViewController
            val activityViewController = UIActivityViewController(listOf(textToShare), null)
            currentViewController?.presentViewController(
                viewControllerToPresent = activityViewController,
                animated = true,
                completion = null
            )
        }
    }
