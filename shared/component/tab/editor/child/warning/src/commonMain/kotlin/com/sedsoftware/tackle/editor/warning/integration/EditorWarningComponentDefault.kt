package com.sedsoftware.tackle.editor.warning.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent.Model
import com.sedsoftware.tackle.editor.warning.store.EditorWarningStore
import com.sedsoftware.tackle.editor.warning.store.EditorWarningStoreProvider
import com.sedsoftware.tackle.utils.extension.asValue

class EditorWarningComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val dispatchers: TackleDispatchers,
) : EditorWarningComponent, ComponentContext by componentContext {

    private val store: EditorWarningStore =
        instanceKeeper.getStore {
            EditorWarningStoreProvider(
                storeFactory = storeFactory,
                mainContext = dispatchers.main,
            ).create()
        }

    override val model: Value<Model> = store.asValue().map(stateToModel)

    override fun onTextInput(text: String) {
        store.accept(EditorWarningStore.Intent.OnTextInput(text))
    }

    override fun onClearTextInput() {
        store.accept(EditorWarningStore.Intent.OnTextInput(""))
    }

    override fun toggleComponentVisibility() {
        store.accept(EditorWarningStore.Intent.ToggleComponentVisibility)
    }

    override fun resetComponentState() {
        store.accept(EditorWarningStore.Intent.ResetState)
    }
}
