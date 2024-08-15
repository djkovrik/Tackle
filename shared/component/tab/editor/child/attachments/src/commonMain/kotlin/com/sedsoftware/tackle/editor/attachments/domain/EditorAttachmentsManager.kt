package com.sedsoftware.tackle.editor.attachments.domain

import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsGateways
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.attachments.model.UploadProgress
import com.sedsoftware.tackle.utils.extension.isImage
import com.sedsoftware.tackle.utils.extension.isVideo
import com.sedsoftware.tackle.utils.generateUUID
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class EditorAttachmentsManager(
    private val api: EditorAttachmentsGateways.Api,
) {
    init {
        Napier.base(DebugAntilog())
    }

    private val uploadProgress: MutableStateFlow<UploadProgress> =
        MutableStateFlow(UploadProgress("", 0f))

    fun observeUploadProgress(): Flow<UploadProgress> = uploadProgress

    fun prepare(platformFile: PlatformFileWrapper, config: Instance.Config): Result<AttachedFile> = runCatching {
        if (platformFile.size == 0L) {
            throw TackleException.FileNotAvailable
        }

        if (!config.mediaAttachments.supportedMimeTypes.contains(platformFile.mimeType)) {
            throw TackleException.FileTypeNotSupported
        }

        val isImage: Boolean = platformFile.isImage
        val isVideo: Boolean = platformFile.isVideo
        val imageSizeLimit: Long = config.mediaAttachments.imageSizeLimit
        val videoSizeLimit: Long = config.mediaAttachments.videoSizeLimit

        val fileSizeExceeded = when {
            isImage && imageSizeLimit != 0L && platformFile.size > imageSizeLimit -> true
            isVideo && videoSizeLimit != 0L && platformFile.size > videoSizeLimit -> true
            else -> false
        }

        if (fileSizeExceeded) {
            throw TackleException.FileSizeExceeded
        }

        return@runCatching AttachedFile(
            id = generateUUID(),
            file = platformFile,
            status = AttachedFile.Status.PENDING,
        )
    }

    suspend fun upload(
        attachment: AttachedFile,
        onUpload: (Float) -> Unit = { updateProgress(attachment.id, it) },
    ): Result<MediaAttachment> = runCatching {
        val response: MediaAttachment = api.sendFile(
            file = attachment.file,
            onUpload = onUpload,
            thumbnail = null,
            description = null,
            focus = null,
        )

        return@runCatching response
    }

    private fun updateProgress(id: String, progress: Float) {
        uploadProgress.value = UploadProgress(id, progress)
        Napier.d("PROGRESS: Current $progress for ID $id")
    }
}
