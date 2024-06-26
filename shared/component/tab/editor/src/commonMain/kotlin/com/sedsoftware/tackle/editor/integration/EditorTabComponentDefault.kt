package com.sedsoftware.tackle.editor.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.EditorTabComponent.Model
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.domain.StatusEditorManager
import com.sedsoftware.tackle.editor.store.EditorStore
import com.sedsoftware.tackle.editor.store.EditorStore.Label
import com.sedsoftware.tackle.editor.store.EditorStoreProvider
import com.sedsoftware.tackle.utils.TackleDispatchers
import com.sedsoftware.tackle.utils.asValue
import com.sedsoftware.tackle.utils.model.AppLocale
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
    private val output: (EditorTabComponent.Output) -> Unit,
) : EditorTabComponent, ComponentContext by componentContext {

    private val store: EditorStore =
        instanceKeeper.getStore {
            EditorStoreProvider(
                storeFactory = storeFactory,
                manager = StatusEditorManager(api, database, settings, tools),
                mainContext = dispatchers.main,
                ioContext = dispatchers.io,
            ).create()
        }

    init {
        val scope = CoroutineScope(dispatchers.main)

        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is Label.ErrorCaught -> output(EditorTabComponent.Output.ErrorCaught(label.throwable))
                }
            }
        }

        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    override fun onTextInput(text: String) {
        store.accept(EditorStore.Intent.OnTextInput(text))
    }

    override fun onEmojiPanelRequest(show: Boolean) {
        store.accept(EditorStore.Intent.OnShowEmojiPanel(show))
    }

    override fun onLanguagePickerRequest(show: Boolean) {
        store.accept(EditorStore.Intent.OnShowLanguagePicker(show))
    }

    override fun onLocaleSelect(language: AppLocale) {
        store.accept(EditorStore.Intent.OnLocaleSelected(language))
    }

    override val model: Value<Model> = store.asValue().map(stateToModel)
}
