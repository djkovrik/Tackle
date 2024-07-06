package com.sedsoftware.tackle.editor.attachments

import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.MediaAttachment
import kotlinx.coroutines.flow.Flow

interface EditorAttachmentsGateways {
    interface Api {
        suspend fun sendFile(
            file: PlatformFileWrapper,
            onUpload : (Int) -> Unit = {},
            thumbnail: PlatformFileWrapper? = null,
            description: String? = null,
            focus: String? = null
        ): MediaAttachment
    }

    interface Database {
        suspend fun getCachedInstanceInfo(): Flow<Instance>
    }
}
