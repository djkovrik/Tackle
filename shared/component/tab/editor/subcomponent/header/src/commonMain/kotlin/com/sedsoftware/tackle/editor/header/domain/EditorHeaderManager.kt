package com.sedsoftware.tackle.editor.header.domain

import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.editor.header.EditorHeaderComponentGateways
import com.sedsoftware.tackle.editor.header.model.EditorProfileData
import com.sedsoftware.tackle.utils.trimUrl

internal class EditorHeaderManager(
    private val settings: EditorHeaderComponentGateways.Settings,
    private val tools: EditorHeaderComponentGateways.Tools,
) {

    fun getEditorProfileData(): Result<EditorProfileData> = runCatching {
        return@runCatching EditorProfileData(
            avatar = settings.ownAvatar,
            name = settings.ownNickname,
            domain = settings.domainShort,
        )
    }

    fun getRecommendedLocale(): Result<AppLocale> = runCatching {
        if (settings.lastSelectedLanguageName.isNotEmpty() && settings.lastSelectedLanguageCode.isNotEmpty()) {
            return@runCatching AppLocale(
                languageName = settings.lastSelectedLanguageName,
                languageCode = settings.lastSelectedLanguageCode,
            )
        }

        return@runCatching tools.getCurrentLocale()
    }

    fun getAvailableLocales(): Result<List<AppLocale>> = runCatching {
        return@runCatching tools.getAvailableLocales()
    }

    fun saveSelectedLocale(locale: AppLocale): Result<AppLocale> = runCatching {
        settings.lastSelectedLanguageName = locale.languageName
        settings.lastSelectedLanguageCode = locale.languageCode
        return@runCatching locale
    }
}
