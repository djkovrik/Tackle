package com.sedsoftware.tackle.editor.header

import com.sedsoftware.tackle.domain.model.AppLocale

interface EditorHeaderComponentGateways {
    interface Settings {
        val ownAvatar: String
        val ownNickname: String
        val domain: String
        var lastSelectedLanguageName: String
        var lastSelectedLanguageCode: String
    }

    interface Tools {
        fun getCurrentLocale(): AppLocale
        fun getAvailableLocales(): List<AppLocale>
    }
}
