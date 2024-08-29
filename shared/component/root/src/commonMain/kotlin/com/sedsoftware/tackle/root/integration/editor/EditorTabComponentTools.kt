package com.sedsoftware.tackle.root.integration.editor

import com.sedsoftware.tackle.domain.api.TacklePlatformTools
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.editor.EditorComponentGateways

internal class EditorTabComponentTools(
    private val platformTools: TacklePlatformTools,
) : EditorComponentGateways.Tools {

    override fun getCurrentLocale(): AppLocale =
        platformTools.getCurrentLocale()

    override fun getAvailableLocales(): List<AppLocale> =
        platformTools.getAvailableLocales()

    override fun getInputHintDelay(): Long = INPUT_HINT_APPEARANCE_DELAY

    private companion object {
        const val INPUT_HINT_APPEARANCE_DELAY = 500L
    }
}
