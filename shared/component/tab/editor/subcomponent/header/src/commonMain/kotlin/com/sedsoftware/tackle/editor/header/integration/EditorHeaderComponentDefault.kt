package com.sedsoftware.tackle.editor.header.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent.Model
import com.sedsoftware.tackle.editor.header.EditorHeaderComponentGateways
import com.sedsoftware.tackle.editor.header.domain.EditorHeaderManager
import com.sedsoftware.tackle.editor.header.store.EditorHeaderStore
import com.sedsoftware.tackle.editor.header.store.EditorHeaderStore.Label
import com.sedsoftware.tackle.editor.header.store.EditorHeaderStoreProvider
import com.sedsoftware.tackle.utils.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class EditorHeaderComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val settings: EditorHeaderComponentGateways.Settings,
    private val tools: EditorHeaderComponentGateways.Tools,
    private val dispatchers: TackleDispatchers,
    private val output: (EditorHeaderComponent.Output) -> Unit,
) : EditorHeaderComponent, ComponentContext by componentContext {

    private val store: EditorHeaderStore =
        instanceKeeper.getStore {
            EditorHeaderStoreProvider(
                storeFactory = storeFactory,
                manager = EditorHeaderManager(settings, tools),
                mainContext = dispatchers.main,
                ioContext = dispatchers.io,
            ).create()
        }

    init {
        val scope = CoroutineScope(dispatchers.main)

        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is Label.ErrorCaught -> output(EditorHeaderComponent.Output.ErrorCaught(label.throwable))
                }
            }
        }

        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    override val model: Value<Model> = store.asValue().map(stateToModel)

    override fun onLocalePickerRequested(show: Boolean) {
        store.accept(EditorHeaderStore.Intent.OnShowLocalePicker(show))
    }

    override fun onLocaleSelected(language: AppLocale) {
        store.accept(EditorHeaderStore.Intent.OnLocaleSelected(language))
    }

    override fun onStatusVisibilityPickerRequested(show: Boolean) {
        store.accept(EditorHeaderStore.Intent.OnShowStatusVisibilityPicker(show))
    }

    override fun onStatusVisibilitySelected(visibility: StatusVisibility) {
        store.accept(EditorHeaderStore.Intent.OnStatusVisibilitySelected(visibility))
    }
}
