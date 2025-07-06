package com.sedsoftware.tackle.editor.attachments.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.StoreCreate
import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.attachments.domain.EditorAttachmentsManager
import com.sedsoftware.tackle.editor.attachments.extension.delete
import com.sedsoftware.tackle.editor.attachments.extension.getById
import com.sedsoftware.tackle.editor.attachments.extension.type
import com.sedsoftware.tackle.editor.attachments.extension.updateProgress
import com.sedsoftware.tackle.editor.attachments.extension.updateServerCopy
import com.sedsoftware.tackle.editor.attachments.extension.updateStatus
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.attachments.model.AttachedFileType
import com.sedsoftware.tackle.editor.attachments.model.UploadProgress
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Intent
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Label
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.State
import com.sedsoftware.tackle.utils.extension.isImage
import com.sedsoftware.tackle.utils.extension.unwrap
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class EditorAttachmentsStoreProvider(
    private val storeFactory: StoreFactory,
    private val manager: EditorAttachmentsManager,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
) {

    @StoreCreate
    fun create(autoInit: Boolean = true): EditorAttachmentsStore =
        object : EditorAttachmentsStore, Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
            name = "EditorAttachmentsStore",
            initialState = State(),
            autoInit = autoInit,
            bootstrapper = coroutineBootstrapper(mainContext) {
                dispatch(Action.ObserveUploadProgress)
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
                val uploadJobs = mutableMapOf<String, Job>()

                onAction<Action.ObserveUploadProgress> {
                    launch {
                        manager.observeUploadProgress()
                            .flowOn(ioContext)
                            .catch { publish(Label.ErrorCaught(it)) }
                            .collect { dispatch(Msg.UploadProgressAvailable(it)) }
                    }
                }

                onAction<Action.PrepareAttachment> {
                    val config = state().config

                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.prepare(it.file, config) },
                            onSuccess = { attachment: AttachedFile ->
                                dispatch(Msg.AttachmentPrepared(attachment))
                                forward(Action.UploadAttachment(attachment))
                            },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                            },
                        )
                    }
                }

                onAction<Action.UploadAttachment> {
                    val target = it.attachment
                    dispatch(Msg.AttachmentStatusChanged(target.id, AttachedFile.Status.LOADING))

                    uploadJobs[target.id] = launch {
                        unwrap(
                            result = withContext(ioContext) { manager.upload(target) },
                            onSuccess = { mediaAttachment: MediaAttachment ->
                                dispatch(Msg.AttachmentStatusChanged(target.id, AttachedFile.Status.LOADED))
                                dispatch(Msg.AttachmentLoaded(target.id, mediaAttachment))
                                publish(Label.LoadedAttachmentsCountUpdated(state().selectedFiles.size))
                            },
                            onError = { throwable: Throwable ->
                                dispatch(Msg.AttachmentStatusChanged(target.id, AttachedFile.Status.ERROR))
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onIntent<Intent.OnFilesSelected> {
                    val currentSelectionSize = state().selectedFiles.size
                    var limit = state().config.statuses.maxMediaAttachments
                    var newSelectionSize = it.files.size
                    val total = currentSelectionSize + newSelectionSize
                    val imagesCount = it.files.count { file -> file.type == AttachedFileType.IMAGE }
                    val nonImagesCount = it.files.count { file -> file.type != AttachedFileType.IMAGE }

                    when {
                        // No attachments + selected multiple different files
                        currentSelectionSize == 0 && it.files.size > 1 && imagesCount > 0 && nonImagesCount > 0 -> {
                            publish(Label.ErrorCaught(TackleException.AttachmentDifferentType))
                            return@onIntent
                        }

                        // No attachments + selected multiple non-images
                        currentSelectionSize == 0 && imagesCount == 0 && nonImagesCount > 1 -> {
                            dispatch(Msg.AttachmentsAtLimit)
                            limit = 1
                        }

                        // Something already attached and different file type selected
                        currentSelectionSize != 0 -> {
                            val firstFileType = state().selectedFiles.first().file.type
                            if (it.files.count { file -> file.type != firstFileType } > 0) {
                                publish(Label.ErrorCaught(TackleException.AttachmentDifferentType))
                                dispatch(Msg.AttachmentsAtLimit)
                                return@onIntent
                            }
                        }
                    }

                    if (total > limit) {
                        publish(Label.ErrorCaught(TackleException.AttachmentsLimitExceeded(limit)))
                        newSelectionSize = limit - currentSelectionSize
                    }

                    it.files.take(newSelectionSize).forEach { file ->
                        forward(Action.PrepareAttachment(file))
                    }

                    publish(Label.PendingAttachmentsCountUpdated(newSelectionSize))
                }

                onIntent<Intent.OnFileDeleted> {
                    val currentAttachmentsCount = state().selectedFiles.size
                    publish(Label.PendingAttachmentsCountUpdated(currentAttachmentsCount - 1))
                    publish(Label.LoadedAttachmentsCountUpdated(currentAttachmentsCount - 1))
                    dispatch(Msg.FileDeleted(it.id))
                    uploadJobs[it.id]?.cancel()
                }

                onIntent<Intent.OnFileRetryClicked> {
                    state().selectedFiles.getById(id = it.id)?.let { target ->
                        dispatch(Msg.AttachmentStatusChanged(target.id, AttachedFile.Status.LOADING))

                        uploadJobs[target.id] = launch {
                            unwrap(
                                result = withContext(ioContext) { manager.upload(target) },
                                onSuccess = { mediaAttachment: MediaAttachment ->
                                    dispatch(Msg.AttachmentStatusChanged(target.id, AttachedFile.Status.LOADED))
                                    dispatch(Msg.AttachmentLoaded(target.id, mediaAttachment))
                                    publish(Label.LoadedAttachmentsCountUpdated(state().selectedFiles.size))
                                },
                                onError = { throwable: Throwable ->
                                    dispatch(Msg.AttachmentStatusChanged(target.id, AttachedFile.Status.ERROR))
                                    publish(Label.ErrorCaught(throwable))
                                }
                            )
                        }
                    }
                }

                onIntent<Intent.ChangeComponentAvailability> { dispatch(Msg.ComponentAvailabilityChanged(it.available)) }

                onIntent<Intent.UpdateInstanceConfig> { dispatch(Msg.InstanceConfigAvailable(it.config)) }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.InstanceConfigAvailable -> copy(
                        config = msg.config,
                        configLoaded = true,
                    )

                    is Msg.ComponentAvailabilityChanged -> copy(
                        attachmentsAvailable = msg.available,
                    )

                    is Msg.UploadProgressAvailable -> copy(
                        selectedFiles = selectedFiles.updateProgress(msg.progress)
                    )

                    is Msg.AttachmentPrepared -> copy(
                        selectedFiles = selectedFiles + listOf(msg.attachment),
                        attachmentsAtLimit = !msg.attachment.file.isImage || selectedFiles.size + 1 >= config.statuses.maxMediaAttachments,
                    )

                    is Msg.FileDeleted -> copy(
                        selectedFiles = selectedFiles.delete(msg.id),
                        attachmentsAtLimit = false,
                    )

                    is Msg.AttachmentStatusChanged -> copy(
                        selectedFiles = selectedFiles.updateStatus(msg.id, msg.status)
                    )

                    is Msg.AttachmentLoaded -> copy(
                        selectedFiles = selectedFiles.updateServerCopy(msg.id, msg.serverAttachment)
                    )

                    is Msg.AttachmentsAtLimit -> copy(
                        attachmentsAtLimit = true,
                    )
                }
            }
        ) {}

    private sealed interface Action {
        data object ObserveUploadProgress : Action
        data class PrepareAttachment(val file: PlatformFileWrapper) : Action
        data class UploadAttachment(val attachment: AttachedFile) : Action
    }

    private sealed interface Msg {
        data class InstanceConfigAvailable(val config: Instance.Config) : Msg
        data class ComponentAvailabilityChanged(val available: Boolean) : Msg
        data class UploadProgressAvailable(val progress: UploadProgress) : Msg
        data class AttachmentPrepared(val attachment: AttachedFile) : Msg
        data class FileDeleted(val id: String) : Msg
        data class AttachmentStatusChanged(val id: String, val status: AttachedFile.Status) : Msg
        data class AttachmentLoaded(val id: String, val serverAttachment: MediaAttachment) : Msg
        data object AttachmentsAtLimit : Msg
    }
}
