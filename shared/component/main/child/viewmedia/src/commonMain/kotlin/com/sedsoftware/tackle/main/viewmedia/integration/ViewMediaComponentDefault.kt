package com.sedsoftware.tackle.main.viewmedia.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponent
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponent.Model
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponentGateways
import com.sedsoftware.tackle.main.viewmedia.domain.FileDownloadManager
import com.sedsoftware.tackle.main.viewmedia.store.ViewMediaStore
import com.sedsoftware.tackle.main.viewmedia.store.ViewMediaStore.Label
import com.sedsoftware.tackle.main.viewmedia.store.ViewMediaStoreProvider
import com.sedsoftware.tackle.utils.extension.asValue
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ViewMediaComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: ViewMediaComponentGateways.Api,
    private val attachments: List<MediaAttachment>,
    private val selectedIndex: Int,
    private val onBackClicked: () -> Unit,
    private val dispatchers: TackleDispatchers,
    private val viewMediaOutput: (ComponentOutput) -> Unit,
) : ViewMediaComponent, ComponentContext by componentContext {

    private val store: ViewMediaStore =
        instanceKeeper.getStore {
            ViewMediaStoreProvider(
                storeFactory = storeFactory,
                manager = FileDownloadManager(api),
                mainContext = dispatchers.main,
                ioContext = dispatchers.io,
            ).create(attachments = attachments, selectedIndex = selectedIndex)
        }

    init {
        val scope = CoroutineScope(dispatchers.main)

        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is Label.ErrorCaught -> viewMediaOutput(ComponentOutput.Common.ErrorCaught(label.exception))
                }
            }
        }

        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    override val model: Value<Model> = store.asValue().map(stateToModel)

    override fun onBack() {
        onBackClicked()
    }

    override fun onDownload(destination: PlatformFile) {
        store.accept(ViewMediaStore.Intent.OnDownloadClicked(destination))
    }

    override fun onSelectNewItem(index: Int) {
        store.accept(ViewMediaStore.Intent.OnSelectionChanged(index))
    }
}
