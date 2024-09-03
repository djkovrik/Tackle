package com.sedsoftware.tackle.editor.header.stubs

import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.editor.header.EditorHeaderComponentGateways

class EditorHeaderToolsStub : EditorHeaderComponentGateways.Tools {

    override fun getCurrentLocale(): AppLocale = AppLocale("English", "en")

    override fun getAvailableLocales(): List<AppLocale> = listOf(
        AppLocale("English", "en"),
        AppLocale("Russian", "ru"),
    )
}
