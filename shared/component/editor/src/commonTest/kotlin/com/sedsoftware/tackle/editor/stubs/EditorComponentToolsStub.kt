package com.sedsoftware.tackle.editor.stubs

import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.editor.EditorComponentGateways

class EditorComponentToolsStub : EditorComponentGateways.Tools {
    override fun getCurrentLocale(): AppLocale = AppLocale("English", "en")
    override fun getAvailableLocales(): List<AppLocale> = listOf(AppLocale("English", "en"))
    override fun getInputHintDelay(): Long = 0L
}
