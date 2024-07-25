package com.sedsoftware.tackle.editor.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.domain.EditorTabManager
import com.sedsoftware.tackle.editor.extension.getNewLength
import com.sedsoftware.tackle.editor.extension.getNewPosition
import com.sedsoftware.tackle.editor.extension.insertEmoji
import com.sedsoftware.tackle.editor.store.EditorTabStore.Intent
import com.sedsoftware.tackle.editor.store.EditorTabStore.Label
import com.sedsoftware.tackle.editor.store.EditorTabStore.State
import com.sedsoftware.tackle.utils.StoreCreate
import com.sedsoftware.tackle.utils.extension.unwrap
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class EditorTabStoreProvider(
    private val storeFactory: StoreFactory,
    private val manager: EditorTabManager,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
) {

    @StoreCreate
    fun create(autoInit: Boolean = true): EditorTabStore =
        object : EditorTabStore, Store<Intent, State, Label> by storeFactory.create<Intent, Action, Msg, State, Label>(
            name = "EditorTabStore",
            initialState = State(),
            autoInit = autoInit,
            bootstrapper = coroutineBootstrapper {
                dispatch(Action.FetchCachedInstanceInfo)
            },
            executorFactory = coroutineExecutorFactory(mainContext) {
                onAction<Action.FetchCachedInstanceInfo> {
                    launch {
                        unwrap(
                            result = withContext(ioContext) { manager.getCachedInstanceInfo() },
                            onSuccess = { cachedInstance: Instance ->
                                dispatch(Msg.CachedInstanceLoaded(cachedInstance))
                                dispatch(Msg.StatusCharactersLimitAvailable(cachedInstance.config.statuses.maxCharacters))
                                publish(Label.InstanceConfigLoaded(cachedInstance.config))
                            },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }

                onIntent<Intent.OnTextInput> { dispatch(Msg.TextInput(it.text, it.selection)) }

                onIntent<Intent.OnEmojiSelect> { dispatch(Msg.EmojiSelected(it.emoji)) }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.CachedInstanceLoaded -> copy(
                        instanceInfo = msg.instance,
                        instanceInfoLoaded = true,
                        statusCharactersLimit = msg.instance.config.statuses.maxCharacters,
                    )

                    is Msg.StatusCharactersLimitAvailable -> copy(
                        statusCharactersLeft = msg.limit,
                    )

                    is Msg.TextInput -> copy(
                        statusText = msg.text.take(statusCharactersLimit),
                        statusTextSelection = if (msg.exceedTheLimit(statusCharactersLimit)) {
                            msg.selection.first to statusCharactersLimit
                        } else {
                            msg.selection
                        },
                        statusCharactersLeft = if (msg.underTheLimit(statusCharactersLimit)) {
                            statusCharactersLimit - msg.text.length
                        } else {
                            0
                        },
                    )

                    is Msg.EmojiSelected -> copy(
                        statusText = statusText.insertEmoji(msg.emoji, this),
                        statusTextSelection = statusText.getNewPosition(msg.emoji, this),
                        statusCharactersLeft = statusCharactersLimit - statusText.getNewLength(msg.emoji, this),
                    )
                }
            }
        ) {}

    private sealed interface Action {
        data object FetchCachedInstanceInfo : Action
    }

    private sealed interface Msg {
        data class CachedInstanceLoaded(val instance: Instance) : Msg
        data class StatusCharactersLimitAvailable(val limit: Int) : Msg
        data class TextInput(val text: String, val selection: Pair<Int, Int>) : Msg
        data class EmojiSelected(val emoji: CustomEmoji) : Msg
    }

    private fun Msg.TextInput.exceedTheLimit(limit: Int): Boolean = limit - text.length < 0
    private fun Msg.TextInput.underTheLimit(limit: Int): Boolean = !exceedTheLimit(limit)
}
