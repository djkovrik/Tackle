package com.sedsoftware.tackle.editor.poll.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.model.Instance.Config
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.poll.EditorPollComponent.Model
import com.sedsoftware.tackle.editor.poll.domain.EditorPollManager
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.poll.store.EditorPollStore
import com.sedsoftware.tackle.editor.poll.store.EditorPollStoreProvider
import com.sedsoftware.tackle.utils.extension.asValue

class EditorPollComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val dispatchers: TackleDispatchers,
) : EditorPollComponent, ComponentContext by componentContext {

    private val store: EditorPollStore =
        instanceKeeper.getStore {
            EditorPollStoreProvider(
                storeFactory = storeFactory,
                manager = EditorPollManager(),
                mainContext = dispatchers.main,
                ioContext = dispatchers.io,
            ).create()
        }

    override val model: Value<Model> = store.asValue().map(stateToModel)

    override fun onDurationPickerRequested(show: Boolean) {
        store.accept(EditorPollStore.Intent.OnRequestDurationPicker(show))
    }

    override fun onDurationSelected(duration: PollDuration) {
        store.accept(EditorPollStore.Intent.OnDurationSelected(duration))
    }

    override fun onMultiselectEnabled(enabled: Boolean) {
        store.accept(EditorPollStore.Intent.OnMultiselectEnabled(enabled))
    }

    override fun onHideTotalsEnabled(enabled: Boolean) {
        store.accept(EditorPollStore.Intent.OnHideTotalsEnabled(enabled))
    }

    override fun onTextInput(id: String, text: String) {
        store.accept(EditorPollStore.Intent.OnTextInput(id, text))
    }

    override fun onAddPollOptionClick() {
        store.accept(EditorPollStore.Intent.OnAddPollOption)
    }

    override fun onDeletePollOptionClick(id: String) {
        store.accept(EditorPollStore.Intent.OnDeletePollOption(id))
    }

    override fun changeComponentAvailability(available: Boolean) {
        store.accept(EditorPollStore.Intent.ChangeComponentAvailability(available))
    }

    override fun toggleComponentVisibility() {
        store.accept(EditorPollStore.Intent.ToggleComponentVisibility)
    }

    override fun updateInstanceConfig(config: Config) {
        store.accept(EditorPollStore.Intent.UpdateInstanceConfig(config))
    }
}
