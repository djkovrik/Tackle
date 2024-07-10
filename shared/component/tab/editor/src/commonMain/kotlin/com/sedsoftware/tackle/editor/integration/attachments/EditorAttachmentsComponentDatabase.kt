package com.sedsoftware.tackle.editor.integration.attachments

import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsGateways
import kotlinx.coroutines.flow.Flow

internal class EditorAttachmentsComponentDatabase(
    private val database: EditorTabComponentGateways.Database,
) : EditorAttachmentsGateways.Database {

    override suspend fun getCachedInstanceInfo(): Flow<Instance> = database.getCachedInstanceInfo()
}
