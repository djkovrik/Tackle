package com.sedsoftware.tackle

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.sedsoftware.tackle.domain.TacklePlatformTools
import com.sedsoftware.tackle.domain.model.AppClientData
import com.sedsoftware.tackle.domain.model.AppLocale
import java.util.Locale

@Suppress("FunctionName")
fun PlatformToolsFactory(context: Context): TacklePlatformTools =
    object : TacklePlatformTools {
        private val languageCodeLength = 2

        override fun openUrl(url: String?) {
            val uri = url?.let { Uri.parse(it) } ?: return
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = uri
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

        override fun getClientData(): AppClientData {
            val scheme = context.getString(R.string.app_scheme)
            val host = context.getString(R.string.app_host)
            return AppClientData(
                name = context.getString(R.string.app_name),
                uri = "$scheme://$host",
                scopes = context.getString(R.string.app_scopes),
                website = context.getString(R.string.app_website),
            )
        }

        override fun getCurrentLocale(): AppLocale {
            val locale: Locale = context.resources.configuration.locales.get(0)

            return AppLocale(
                languageName = locale.displayLanguage.capitalizeDisplayName(locale),
                languageCode = locale.language
            )
        }

        override fun getAvailableLocales(): List<AppLocale> {
            val locale: Locale = context.resources.configuration.locales.get(0)
            val locales: Array<Locale> = Locale.getAvailableLocales()

            return locales
                .filter { it.language.length == languageCodeLength }
                .distinctBy { it.language }
                .map { AppLocale(languageName = it.displayLanguage.capitalizeDisplayName(locale), languageCode = it.language) }
        }

        fun String.capitalizeDisplayName(locale: Locale): String =
            this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    }
