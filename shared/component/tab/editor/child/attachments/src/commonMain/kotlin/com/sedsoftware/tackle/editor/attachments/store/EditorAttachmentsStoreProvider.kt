package com.sedsoftware.tackle.editor.attachments.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.domain.EditorAttachmentsManager
import com.sedsoftware.tackle.editor.attachments.extension.delete
import com.sedsoftware.tackle.editor.attachments.extension.firstPending
import com.sedsoftware.tackle.editor.attachments.extension.hasPending
import com.sedsoftware.tackle.editor.attachments.extension.updateProgress
import com.sedsoftware.tackle.editor.attachments.extension.updateServerCopy
import com.sedsoftware.tackle.editor.attachments.extension.updateStatus
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.attachments.model.UploadProgress
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Intent
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Label
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.State
import com.sedsoftware.tackle.utils.StoreCreate
import com.sedsoftware.tackle.utils.extension.unwrap
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
            bootstrapper = coroutineBootstrapper {
                dispatch(Action.ObserveUploadProgress)
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
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
                            },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                            },
                        )
                    }
                }

                onAction<Action.UploadNextPendingAttachment> {
                    val current: List<AttachedFile> = state().selectedFiles

                    if (current.hasPending) {
                        val target: AttachedFile = current.firstPending
                        dispatch(Msg.AttachmentStatusChanged(target.id, AttachedFile.Status.LOADING))

                        launch {
                            unwrap(
                                result = withContext(ioContext) { manager.upload(target) },
                                onSuccess = { mediaAttachment: MediaAttachment ->
                                    dispatch(Msg.AttachmentStatusChanged(target.id, AttachedFile.Status.LOADED))
                                    dispatch(Msg.AttachmentLoaded(target.id, mediaAttachment))
                                    forward(Action.UploadNextPendingAttachment)
                                },
                                onError = { throwable: Throwable ->
                                    dispatch(Msg.AttachmentStatusChanged(target.id, AttachedFile.Status.ERROR))
                                    publish(Label.ErrorCaught(throwable))
                                    forward(Action.UploadNextPendingAttachment)
                                }
                            )
                        }
                    } else {
                        dispatch(Msg.UploadQueueCompleted)
                    }
                }

                onIntent<Intent.OnFilesSelected> {
                    val currentSelectionSize = state().selectedFiles.size
                    val limit = state().config.statuses.maxMediaAttachments
                    var newSelectionSize = it.files.size
                    val total = currentSelectionSize + newSelectionSize

                    if (total > limit) {
                        publish(Label.ErrorCaught(TackleException.AttachmentsLimitExceeded(limit)))
                        newSelectionSize = limit - currentSelectionSize
                    }

                    it.files.take(newSelectionSize).forEach { file ->
                        forward(Action.PrepareAttachment(file))
                    }

                    if (newSelectionSize > 0) {
                        dispatch(Msg.UploadQueueStarted)
                        forward(Action.UploadNextPendingAttachment)
                    }

                    publish(Label.AttachmentsCountUpdated(newSelectionSize))
                }

                onIntent<Intent.OnFileDeleted> {
                    val currentAttachmentsCount = state().selectedFiles.size
                    publish(Label.AttachmentsCountUpdated(currentAttachmentsCount - 1))
                    dispatch(Msg.FileDeleted(it.id))
                }

                onIntent<Intent.ChangeFeatureState> { dispatch(Msg.FeatureStateChanged(it.available)) }

                onIntent<Intent.UpdateInstanceConfig> { dispatch(Msg.InstanceConfigAvailable(it.config)) }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.InstanceConfigAvailable -> copy(
                        config = msg.config,
                        configLoaded = true,
                    )

                    is Msg.UploadProgressAvailable -> copy(
                        selectedFiles = selectedFiles.updateProgress(msg.progress)
                    )

                    is Msg.AttachmentPrepared -> copy(
                        selectedFiles = selectedFiles + listOf(msg.attachment),
                        attachmentsAtLimit = (selectedFiles.size + 1) >= config.statuses.maxMediaAttachments,
                    )

                    is Msg.AttachmentStatusChanged -> copy(
                        selectedFiles = selectedFiles.updateStatus(msg.id, msg.status)
                    )

                    is Msg.AttachmentLoaded -> copy(
                        selectedFiles = selectedFiles.updateServerCopy(msg.id, msg.serverAttachment)
                    )

                    is Msg.UploadQueueStarted -> copy(
                        hasUploadInProgress = true,
                    )

                    is Msg.UploadQueueCompleted -> copy(
                        hasUploadInProgress = false,
                    )

                    is Msg.FeatureStateChanged -> copy(
                        attachmentsAvailable = msg.available,
                    )

                    is Msg.FileDeleted -> copy(
                        selectedFiles = selectedFiles.delete(msg.id),
                        attachmentsAtLimit = false,
                    )
                }
            }
        ) {}

    private sealed interface Action {
        data object ObserveUploadProgress : Action
        data class PrepareAttachment(val file: PlatformFileWrapper) : Action
        data object UploadNextPendingAttachment : Action
    }

    private sealed interface Msg {
        data class InstanceConfigAvailable(val config: Instance.Config) : Msg
        data class UploadProgressAvailable(val progress: UploadProgress) : Msg
        data class AttachmentPrepared(val attachment: AttachedFile) : Msg
        data class AttachmentStatusChanged(val id: String, val status: AttachedFile.Status) : Msg
        data class AttachmentLoaded(val id: String, val serverAttachment: MediaAttachment) : Msg
        data object UploadQueueStarted : Msg
        data object UploadQueueCompleted : Msg
        data class FeatureStateChanged(val available: Boolean) : Msg
        data class FileDeleted(val id: String) : Msg
    }
}
