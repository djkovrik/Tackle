package com.sedsoftware.tackle.editor.integration.header

import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.editor.EditorComponentGateways
import com.sedsoftware.tackle.editor.header.EditorHeaderComponentGateways

internal class EditorHeaderComponentTools(
    private val tools: EditorComponentGateways.Tools,
) : EditorHeaderComponentGateways.Tools {

    override fun getCurrentLocale(): AppLocale = tools.getCurrentLocale()
    override fun getAvailableLocales(): List<AppLocale> = tools.getAvailableLocales()
}
