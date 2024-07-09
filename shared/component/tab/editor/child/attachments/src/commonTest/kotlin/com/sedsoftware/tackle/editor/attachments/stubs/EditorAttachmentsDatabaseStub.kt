package com.sedsoftware.tackle.editor.attachments.stubs

import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsGateways
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EditorAttachmentsDatabaseStub : EditorAttachmentsGateways.Database {

    override suspend fun getCachedInstanceInfo(): Flow<Instance> = flowOf(
        Instance.empty().copy(config = InstanceConfigStub.config)
    )
}
