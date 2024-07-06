package com.sedsoftware.tackle.editor.attachments.extension

import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.attachments.model.UploadProgress

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

internal fun List<AttachedFile>.delete(id: String): List<AttachedFile> =
    filterNot { it.id == id }

internal fun List<AttachedFile>.get(id: String): AttachedFile? =
    firstOrNull { it.id == id }
