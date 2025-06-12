package com.sedsoftware.tackle.editor.details.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.StoreCreate
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.details.domain.EditorAttachmentDetailsManager
import com.sedsoftware.tackle.editor.details.model.AttachmentParams
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore.Intent
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore.Label
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore.State
import com.sedsoftware.tackle.utils.extension.unwrap
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class EditorAttachmentDetailsStoreProvider(
    private val storeFactory: StoreFactory,
    private val manager: EditorAttachmentDetailsManager,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
) {
    @StoreCreate
    fun create(
        attachmentType: MediaAttachmentType,
        attachmentUrl: String,
        attachmentId: String,
        attachmentImageParams: AttachmentParams,
        autoInit: Boolean = true,
    ): EditorAttachmentDetailsStore =
        object : EditorAttachmentDetailsStore, Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
            name = "EditorAttachmentDetailsStore",
            initialState = State(
                type = attachmentType,
                url = attachmentUrl,
                id = attachmentId,
                params = attachmentImageParams,
            ),
            autoInit = autoInit,
            bootstrapper = coroutineBootstrapper(mainContext) {
                dispatch(Action.LoadAttachmentData)
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
                onAction<Action.LoadAttachmentData> {
                    launch {
                        dispatch(Msg.LoadingActive(true))

                        unwrap(
                            result = withContext(ioContext) { manager.getFile(attachmentId) },
                            onSuccess = { attachment: MediaAttachment ->
                                dispatch(Msg.LoadingActive(false))
                                dispatch(Msg.AttachmentLoaded(attachment))
                            },
                            onError = { throwable: Throwable ->
                                dispatch(Msg.LoadingActive(false))
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onIntent<Intent.SendAttachmentUpdate> {
                    launch {
                        val state = state()
                        dispatch(Msg.LoadingActive(true))

                        unwrap(
                            result = withContext(ioContext) { manager.updateFile(state.id, state.description, state.focus) },
                            onSuccess = { _: MediaAttachment ->
                                dispatch(Msg.LoadingActive(false))
                                publish(Label.AttachmentDataUpdated)
                            },
                            onError = { throwable: Throwable ->
                                dispatch(Msg.LoadingActive(false))
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onIntent<Intent.OnAlternateTextInput> { dispatch(Msg.DescriptionInput(it.text)) }

                onIntent<Intent.OnFocusInput> { dispatch(Msg.FocusInput(it.x to it.y)) }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.AttachmentLoaded -> copy(
                        initialDescription = msg.attachment.description,
                        description = msg.attachment.description,
                        initialFocus = msg.attachment.meta?.focus?.let { it.x to it.y } ?: (0f to 0f),
                        focus = msg.attachment.meta?.focus?.let { it.x to it.y } ?: (0f to 0f),
                    )

                    is Msg.DescriptionInput -> copy(
                        description = msg.text,
                        dataChanged = msg.text != initialDescription || focus != initialFocus,
                    )

                    is Msg.FocusInput -> copy(
                        focus = msg.focus,
                        dataChanged = msg.focus != initialFocus || description != initialDescription,
                    )

                    is Msg.LoadingActive -> copy(
                        loading = msg.active,
                    )
                }
            },
        ) {}

    private sealed interface Action {
        data object LoadAttachmentData : Action
    }

    private sealed interface Msg {
        data class AttachmentLoaded(val attachment: MediaAttachment) : Msg
        data class DescriptionInput(val text: String) : Msg
        data class FocusInput(val focus: Pair<Float, Float>) : Msg
        data class LoadingActive(val active: Boolean) : Msg
    }
}
