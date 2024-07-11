package com.sedsoftware.tackle.editor.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.integration.EditorAttachmentsComponentDefault
import com.sedsoftware.tackle.editor.domain.EditorTabManager
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.emojis.integration.EditorEmojisComponentDefault
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.header.integration.EditorHeaderComponentDefault
import com.sedsoftware.tackle.editor.integration.attachments.EditorAttachmentsComponentApi
import com.sedsoftware.tackle.editor.integration.emojis.EditorEmojisComponentApi
import com.sedsoftware.tackle.editor.integration.emojis.EditorEmojisComponentDatabase
import com.sedsoftware.tackle.editor.integration.emojis.EditorEmojisComponentSettings
import com.sedsoftware.tackle.editor.integration.header.EditorHeaderComponentSettings
import com.sedsoftware.tackle.editor.integration.header.EditorHeaderComponentTools
import com.sedsoftware.tackle.editor.store.EditorTabStore
import com.sedsoftware.tackle.editor.store.EditorTabStore.Label
import com.sedsoftware.tackle.editor.store.EditorTabStoreProvider
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent
import com.sedsoftware.tackle.editor.warning.integration.EditorWarningComponentDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class EditorTabComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: EditorTabComponentGateways.Api,
    private val database: EditorTabComponentGateways.Database,
    private val settings: EditorTabComponentGateways.Settings,
    private val tools: EditorTabComponentGateways.Tools,
    private val dispatchers: TackleDispatchers,
    private val editorOutput: (ComponentOutput) -> Unit,
) : EditorTabComponent, ComponentContext by componentContext {

    override val attachments: EditorAttachmentsComponent =
        EditorAttachmentsComponentDefault(
            componentContext = childContext(
                key = "Editor attachments",
                lifecycle = lifecycle
            ),
            storeFactory = storeFactory,
            api = EditorAttachmentsComponentApi(api),
            dispatchers = dispatchers,
            output = ::onChildOutput,
        )

    override val emojis: EditorEmojisComponent =
        EditorEmojisComponentDefault(
            componentContext = childContext(
                key = "Editor emojis",
                lifecycle = lifecycle
            ),
            storeFactory = storeFactory,
            api = EditorEmojisComponentApi(api),
            database = EditorEmojisComponentDatabase(database),
            settings = EditorEmojisComponentSettings(settings),
            dispatchers = dispatchers,
            output = ::onChildOutput,
        )

    override val header: EditorHeaderComponent =
        EditorHeaderComponentDefault(
            componentContext = childContext(
                key = "Editor header",
                lifecycle = lifecycle
            ),
            storeFactory = storeFactory,
            settings = EditorHeaderComponentSettings(settings),
            tools = EditorHeaderComponentTools(tools),
            dispatchers = dispatchers,
            output = ::onChildOutput,
        )

    override val warning: EditorWarningComponent =
        EditorWarningComponentDefault(
            componentContext = childContext(
                key = "Editor warning",
                lifecycle = lifecycle
            ),
            storeFactory = storeFactory,
            dispatchers = dispatchers,
        )

    private val store: EditorTabStore =
        instanceKeeper.getStore {
            EditorTabStoreProvider(
                storeFactory = storeFactory,
                manager = EditorTabManager(database),
                mainContext = dispatchers.main,
                ioContext = dispatchers.io,
            ).create()
        }

    init {
        val scope = CoroutineScope(dispatchers.main)

        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is Label.InstanceConfigLoaded -> attachments.updateInstanceConfig(label.config)
                    is Label.ErrorCaught -> editorOutput(ComponentOutput.Common.ErrorCaught(label.throwable))
                }
            }
        }

        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    private fun onChildOutput(output: ComponentOutput) {
        when (output) {
            is ComponentOutput.StatusEditor.EmojiSelected -> TODO("Emoji selected")
            else -> editorOutput(output)
        }
    }
}
