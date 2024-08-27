package com.sedsoftware.tackle.editor.warning.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.editor.warning.store.EditorWarningStore.Intent
import com.sedsoftware.tackle.editor.warning.store.EditorWarningStore.State
import com.sedsoftware.tackle.utils.StoreCreate
import kotlin.coroutines.CoroutineContext

internal class EditorWarningStoreProvider(
    private val storeFactory: StoreFactory,
    private val mainContext: CoroutineContext,
) {

    @StoreCreate
    fun create(autoInit: Boolean = true): EditorWarningStore =
        object : EditorWarningStore, Store<Intent, State, Nothing> by storeFactory.create<Intent, Nothing, Msg, State, Nothing>(
            name = "EditorWarningStore",
            initialState = State(),
            autoInit = autoInit,
            executorFactory = coroutineExecutorFactory(mainContext) {
                onIntent<Intent.OnTextInput> { dispatch(Msg.TextInput(it.text)) }

                onIntent<Intent.ToggleComponentVisibility> { dispatch(Msg.ComponentVisibilityToggled) }

                onIntent<Intent.ResetState> { dispatch(Msg.StateReset) }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.TextInput -> copy(
                        text = msg.text,
                    )

                    is Msg.ComponentVisibilityToggled -> copy(
                        warningVisible = !warningVisible,
                    )

                    is Msg.StateReset -> copy(
                        text = "",
                        warningVisible = false,
                    )
                }
            }
        ) {}

    private sealed interface Msg {
        data class TextInput(val text: String) : Msg
        data object ComponentVisibilityToggled : Msg
        data object StateReset : Msg
    }
}
