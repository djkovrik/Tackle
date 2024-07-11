package com.sedsoftware.tackle.editor.domain

import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import kotlinx.coroutines.flow.first

internal class EditorTabManager(
    private val database: EditorTabComponentGateways.Database,
) {

    suspend fun getCachedInstanceInfo(): Result<Instance> = runCatching {
        database.getCachedInstanceInfo().first()
    }
}
