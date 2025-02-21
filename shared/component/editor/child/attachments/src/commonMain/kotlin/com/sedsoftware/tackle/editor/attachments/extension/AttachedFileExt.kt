package com.sedsoftware.tackle.editor.attachments.extension

import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.attachments.model.AttachedFileType
import com.sedsoftware.tackle.editor.attachments.model.UploadProgress
import com.sedsoftware.tackle.utils.FileUtils
import com.sedsoftware.tackle.utils.extension.isAudio
import com.sedsoftware.tackle.utils.extension.isImage
import com.sedsoftware.tackle.utils.extension.isVideo
import com.sedsoftware.tackle.utils.extension.orZero
import com.sedsoftware.tackle.utils.extension.toHumanReadableSize

internal val List<AttachedFile>.hasPending: Boolean
    get() = count { it.status == AttachedFile.Status.PENDING } > 0

internal val List<AttachedFile>.hasActiveUpload: Boolean
    get() = count { it.status == AttachedFile.Status.LOADING } > 0

internal val List<AttachedFile>.firstPending: AttachedFile
    get() = first { it.status == AttachedFile.Status.PENDING }

internal fun List<AttachedFile>.updateStatus(id: String, newStatus: AttachedFile.Status): List<AttachedFile> =
    map { if (it.id == id) it.copy(status = newStatus) else it }

internal fun List<AttachedFile>.updateServerCopy(id: String, newCopy: MediaAttachment): List<AttachedFile> =
    map { if (it.id == id) it.copy(serverCopy = newCopy) else it }

internal fun List<AttachedFile>.updateProgress(progress: UploadProgress): List<AttachedFile> =
    map { if (it.id == progress.id) it.copy(uploadProgress = progress.value) else it }

internal fun List<AttachedFile>.getById(id: String): AttachedFile? =
    firstOrNull { it.id == id }

internal fun List<AttachedFile>.delete(id: String): List<AttachedFile> =
    filterNot { it.id == id }

internal val PlatformFileWrapper.type: AttachedFileType
    get() = when {
        isAudio -> AttachedFileType.AUDIO
        isImage -> AttachedFileType.IMAGE
        isVideo -> AttachedFileType.VIDEO
        else -> AttachedFileType.FILE
    }

internal fun wrap(
    name: String,
    extension: String,
    path: String?,
    getSize: () -> Long?,
    readBytes: suspend () -> ByteArray,
): PlatformFileWrapper {
    val size = getSize()

    return PlatformFileWrapper(
        name = name,
        extension = extension,
        path = path.orEmpty(),
        mimeType = FileUtils.getMimeTypeByExtension(extension),
        size = size.orZero(),
        sizeLabel = size.toHumanReadableSize(),
        readBytes = readBytes,
    )
}
