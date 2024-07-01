package com.sedsoftware.tackle.editor.content.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.editor.content.EditorEmojisComponent
import com.sedsoftware.tackle.editor.content.EditorEmojisComponent.Model
import com.sedsoftware.tackle.editor.content.EditorEmojisGateways
import com.sedsoftware.tackle.editor.content.EditorEmojisGateways.Database
import com.sedsoftware.tackle.editor.content.domain.EditorEmojisManager
import com.sedsoftware.tackle.editor.content.store.EditorEmojisStore
import com.sedsoftware.tackle.editor.content.store.EditorEmojisStore.Label
import com.sedsoftware.tackle.editor.content.store.EditorEmojisStoreProvider
import com.sedsoftware.tackle.utils.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class EditorEmojisComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: EditorEmojisGateways.Api,
    private val database: Database,
    private val settings: EditorEmojisGateways.Settings,
    private val dispatchers: TackleDispatchers,
    private val output: (ComponentOutput) -> Unit,
) : EditorEmojisComponent, ComponentContext by componentContext {

    private val store: EditorEmojisStore =
        instanceKeeper.getStore {
            EditorEmojisStoreProvider(
                storeFactory = storeFactory,
                manager = EditorEmojisManager(api, database, settings),
                mainContext = dispatchers.main,
                ioContext = dispatchers.io,
            ).create()
        }

    init {
        val scope = CoroutineScope(dispatchers.main)

        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is Label.ErrorCaught -> output(ComponentOutput.Common.ErrorCaught(label.throwable))
                }
            }
        }

        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    override val model: Value<Model> = store.asValue().map(stateToModel)

    override fun onEmojiClicked(emoji: CustomEmoji) {
        output(ComponentOutput.StatusEditor.EmojiSelected(emoji))
    }
}
