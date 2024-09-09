package com.sedsoftware.tackle.editor.header.stubs

import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.editor.header.EditorHeaderComponentGateways
import com.sedsoftware.tackle.utils.test.BaseStub

class EditorHeaderToolsStub : BaseStub(), EditorHeaderComponentGateways.Tools {

    override fun getCurrentLocale(): AppLocale = asResponse(AppLocale("English", "en"))

    override fun getAvailableLocales(): List<AppLocale> = asResponse(
        listOf(
            AppLocale("English", "en"),
            AppLocale("Russian", "ru"),
        )
    )
}
