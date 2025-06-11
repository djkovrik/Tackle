package com.sedsoftware.tackle

import com.sedsoftware.tackle.domain.api.TacklePlatformTools
import com.sedsoftware.tackle.domain.model.AppClientData
import com.sedsoftware.tackle.domain.model.AppLocale
import java.awt.Desktop
import java.net.URI
import java.util.Locale
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection

@Suppress("FunctionName")
fun PlatformToolsFactory(): TacklePlatformTools =
    object : TacklePlatformTools {
        private val languageCodeLength = 2

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

        override fun getCurrentLocale(): AppLocale {
            val locale: Locale = Locale.getDefault()

            return AppLocale(
                languageName = locale.displayLanguage.capitalizeDisplayName(locale),
                languageCode = locale.language
            )
        }

        override fun getAvailableLocales(): List<AppLocale> {
            val locale: Locale = Locale.getDefault()
            val locales: Array<Locale> = Locale.getAvailableLocales()

            return locales
                .filter { it.language.length == languageCodeLength }
                .distinctBy { it.language }
                .map { AppLocale(languageName = it.displayLanguage.capitalizeDisplayName(locale), languageCode = it.language) }
        }

        override fun shareStatus(title: String, url: String) {
            val textToCopy = "$title\n$url"
            val selection = StringSelection(textToCopy)
            val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
            clipboard.setContents(selection, selection)
        }

        private fun String.capitalizeDisplayName(locale: Locale): String =
            this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    }
