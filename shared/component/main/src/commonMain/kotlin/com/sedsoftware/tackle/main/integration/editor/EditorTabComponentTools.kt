package com.sedsoftware.tackle.main.integration.editor

import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.utils.TacklePlatformTools
import com.sedsoftware.tackle.utils.model.AppLocale

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
