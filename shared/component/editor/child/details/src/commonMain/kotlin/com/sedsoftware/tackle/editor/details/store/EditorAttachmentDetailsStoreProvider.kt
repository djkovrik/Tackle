package com.sedsoftware.tackle.editor.details.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.StoreCreate
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.editor.details.domain.EditorAttachmentDetailsManager
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
        attachmentId: String,
        initialDescription: String,
        initialFocus: Pair<Float, Float>,
        autoInit: Boolean = true,
    ): EditorAttachmentDetailsStore =
        object : EditorAttachmentDetailsStore, Store<Intent, State, Label> by storeFactory.create<Intent, Nothing, Msg, State, Label>(
            name = "EditorAttachmentDetailsStore",
            initialState = State(
                attachmentId = attachmentId,
                initialDescription = initialDescription,
                description = initialDescription,
                initialFocus = initialFocus,
                focus = initialFocus,
            ),
            autoInit = autoInit,
            executorFactory = coroutineExecutorFactory(mainContext) {
                onIntent<Intent.OnAlternateTextInput> { dispatch(Msg.DescriptionInput(it.text)) }

                onIntent<Intent.OnFocusInput> { dispatch(Msg.FocusInput(it.x to it.y)) }

                onIntent<Intent.SendAttachmentUpdate> {
                    launch {
                        val state = state()
                        dispatch(Msg.UpdatingActive(true))

                        unwrap(
                            result = withContext(ioContext) { manager.updateFile(state.attachmentId, state.description, state.focus) },
                            onSuccess = { _: MediaAttachment ->
                                dispatch(Msg.UpdatingActive(false))
                                publish(Label.AttachmentDataUpdated)
                            },
                            onError = { throwable: Throwable ->
                                dispatch(Msg.UpdatingActive(false))
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.DescriptionInput -> copy(
                        description = msg.text,
                        updatingAvailable = msg.text != initialDescription || focus != initialFocus,
                    )

                    is Msg.FocusInput -> copy(
                        focus = msg.focus,
                        updatingAvailable = msg.focus != initialFocus || description != initialDescription,
                    )

                    is Msg.UpdatingActive -> copy(
                        sending = msg.active,
                    )
                }
            },
        ) {}


    private sealed interface Msg {
        data class DescriptionInput(val text: String) : Msg
        data class FocusInput(val focus: Pair<Float, Float>) : Msg
        data class UpdatingActive(val active: Boolean) : Msg
    }
}
