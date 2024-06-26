package com.sedsoftware.tackle.main.integration.editor

import com.sedsoftware.tackle.domain.TacklePlatformTools
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.editor.EditorTabComponentGateways

internal class EditorTabComponentTools(
    private val platformTools: TacklePlatformTools,
) : EditorTabComponentGateways.Tools {

    override fun getCurrentLocale(): AppLocale {
        TODO("Not yet implemented")
    }

    override fun getAvailableLocales(): List<AppLocale> {
        TODO("Not yet implemented")
    }
}
