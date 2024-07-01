package com.sedsoftware.tackle.editor.content.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.content.domain.EditorEmojisManager
import com.sedsoftware.tackle.editor.content.store.EditorEmojisStore.Label
import com.sedsoftware.tackle.editor.content.store.EditorEmojisStore.State
import com.sedsoftware.tackle.utils.StoreCreate
import com.sedsoftware.tackle.utils.unwrap
import kotlinx.coroutines.flow.catch
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
        object : EditorEmojisStore, Store<Nothing, State, Label> by storeFactory.create<Nothing, Action, Msg, State, Label>(
            name = "EditorEmojisStore",
            initialState = State(),
            autoInit = autoInit,
            bootstrapper = coroutineBootstrapper {
                dispatch(Action.FetchServerEmojis)
                dispatch(Action.ObserveCachedEmojis)
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
                onAction<Action.FetchServerEmojis> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.fetchServerEmojis() },
                            onSuccess = {},
                            onError = { throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onAction<Action.ObserveCachedEmojis> {
                    launch {
                        manager.observeCachedEmojis()
                            .catch { publish(Label.ErrorCaught(it)) }
                            .collect { dispatch(Msg.EmojisListUpdated(it)) }
                    }
                }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.EmojisListUpdated -> copy(
                        emojis = msg.list,
                        emojiPickerAvailable = msg.list.isNotEmpty(),
                    )
                }
            }
        ) {}

    private sealed interface Action {
        data object FetchServerEmojis : Action
        data object ObserveCachedEmojis : Action
    }

    private sealed interface Msg {
        data class EmojisListUpdated(val list: List<CustomEmoji>) : Msg
    }
}
