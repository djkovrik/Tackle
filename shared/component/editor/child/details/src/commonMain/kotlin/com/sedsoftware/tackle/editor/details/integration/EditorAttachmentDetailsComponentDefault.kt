package com.sedsoftware.tackle.editor.details.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent.Model
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsGateways
import com.sedsoftware.tackle.editor.details.domain.EditorAttachmentDetailsManager
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStore.Label
import com.sedsoftware.tackle.editor.details.store.EditorAttachmentDetailsStoreProvider
import com.sedsoftware.tackle.utils.extension.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class EditorAttachmentDetailsComponentDefault(
    attachmentId: String,
    initialDescription: String,
    initialFocus: Pair<Float, Float>,
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: EditorAttachmentDetailsGateways.Api,
    private val dispatchers: TackleDispatchers,
    private val output: (ComponentOutput) -> Unit,
    private val onDismiss: () -> Unit,
) : EditorAttachmentDetailsComponent, ComponentContext by componentContext {

    private val store: EditorAttachmentDetailsStore =
        instanceKeeper.getStore {
            EditorAttachmentDetailsStoreProvider(
                storeFactory = storeFactory,
                manager = EditorAttachmentDetailsManager(api),
                mainContext = dispatchers.main,
                ioContext = dispatchers.io,
            ).create(attachmentId, initialDescription, initialFocus)
        }

    init {
        val scope = CoroutineScope(dispatchers.main)

        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is Label.AttachmentDataUpdated -> output(ComponentOutput.StatusEditor.AttachmentDataUpdated)
                    is Label.ErrorCaught -> output(ComponentOutput.Common.ErrorCaught(label.throwable))
                }
            }
        }

        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    override val model: Value<Model> = store.asValue().map(stateToModel)

    override fun onAttachmentDescriptionInput(text: String) {
        store.accept(EditorAttachmentDetailsStore.Intent.OnAlternateTextInput(text))
    }

    override fun onAttachmentFocusInput(x: Float, y: Float) {
        store.accept(EditorAttachmentDetailsStore.Intent.OnFocusInput(x, y))
    }

    override fun onUpdateButtonClicked() {
        store.accept(EditorAttachmentDetailsStore.Intent.SendAttachmentUpdate)
    }

    override fun onBackButtonClicked() {
        onDismiss.invoke()
    }
}
