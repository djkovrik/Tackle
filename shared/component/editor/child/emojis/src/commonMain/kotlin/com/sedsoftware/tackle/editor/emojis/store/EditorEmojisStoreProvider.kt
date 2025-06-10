package com.sedsoftware.tackle.editor.emojis.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.StoreCreate
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.emojis.domain.EditorEmojisManager
import com.sedsoftware.tackle.editor.emojis.store.EditorEmojisStore.Intent
import com.sedsoftware.tackle.editor.emojis.store.EditorEmojisStore.Label
import com.sedsoftware.tackle.editor.emojis.store.EditorEmojisStore.State
import com.sedsoftware.tackle.utils.extension.unwrap
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class EditorEmojisStoreProvider(
    private val storeFactory: StoreFactory,
    private val manager: EditorEmojisManager,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
) {

    @StoreCreate
    fun create(autoInit: Boolean = true): EditorEmojisStore =
        object : EditorEmojisStore, Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
            name = "EditorEmojisStore",
            initialState = State(),
            autoInit = autoInit,
            bootstrapper = coroutineBootstrapper(mainContext) {
                dispatch(Action.FetchServerEmojis)
                dispatch(Action.ObserveCachedEmojis)
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
                onAction<Action.FetchServerEmojis> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.fetchServerEmojis() },
                            onSuccess = {},
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onAction<Action.ObserveCachedEmojis> {
                    launch {
                        manager.observeCachedEmojis()
                            .flowOn(ioContext)
                            .catch { publish(Label.ErrorCaught(it)) }
                            .collect { dispatch(Msg.EmojisListUpdated(it)) }
                    }
                }

                onIntent<Intent.ToggleComponentVisibility> { dispatch(Msg.ComponentVisibilityToggled) }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.EmojisListUpdated -> copy(
                        emojis = msg.emojis,
                            emojisAvailable = msg.emojis.isNotEmpty(),
                    )

                    is Msg.ComponentVisibilityToggled -> copy(
                        emojisVisible = !emojisVisible,
                    )
                }
            }
        ) {}

    private sealed interface Action {
        data object FetchServerEmojis : Action
        data object ObserveCachedEmojis : Action
    }

    private sealed interface Msg {
        data class EmojisListUpdated(val emojis: Map<String, List<CustomEmoji>>) : Msg
        data object ComponentVisibilityToggled : Msg
    }
}
