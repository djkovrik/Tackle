package com.sedsoftware.tackle.editor.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.editor.domain.EditorTabManager
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
                                publish(Label.InstanceConfigLoaded(cachedInstance.config))
                            },
                            onError = { throwable: Throwable ->
                                publish(Label.ErrorCaught(throwable))
                            }
                        )
                    }
                }
            },
            reducer = { msg ->
                when (msg) {
                    is Msg.CachedInstanceLoaded -> copy(
                        instanceInfo = msg.instance,
                        instanceInfoLoaded = true,
                    )
                }
            }
        ) {}

    private sealed interface Action {
        data object FetchCachedInstanceInfo : Action
    }

    private sealed interface Msg {
        data class CachedInstanceLoaded(val instance: Instance) : Msg
    }
}
