package com.sedsoftware.tackle.editor.domain

import com.sedsoftware.tackle.editor.EditorTabComponentGateways

internal class StatusEditorManager(
    private val api: EditorTabComponentGateways.Api,
    private val database: EditorTabComponentGateways.Database,
    private val settings: EditorTabComponentGateways.Settings,
) {
}
