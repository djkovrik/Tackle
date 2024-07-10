package com.sedsoftware.tackle.editor.attachments.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.api.TackleDispatchers
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent.Model
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsGateways
import com.sedsoftware.tackle.editor.attachments.domain.EditorAttachmentsManager
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Label
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStoreProvider
import com.sedsoftware.tackle.utils.FileUtils
import com.sedsoftware.tackle.utils.extension.asValue
import com.sedsoftware.tackle.utils.extension.orZero
import io.github.vinceglb.filekit.core.PlatformFile
import io.github.vinceglb.filekit.core.extension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class EditorAttachmentsComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: EditorAttachmentsGateways.Api,
    private val database: EditorAttachmentsGateways.Database,
    private val dispatchers: TackleDispatchers,
    private val output: (ComponentOutput) -> Unit,
) : EditorAttachmentsComponent, ComponentContext by componentContext {

    private val store: EditorAttachmentsStore =
        instanceKeeper.getStore {
            EditorAttachmentsStoreProvider(
                storeFactory = storeFactory,
                manager = EditorAttachmentsManager(api, database),
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


    override fun onFileSelected(files: List<PlatformFile>) {
        val wrapped: List<PlatformFileWrapper> = files.map(::wrap)
        store.accept(EditorAttachmentsStore.Intent.OnFilesSelected(wrapped))
    }

    override fun changeFeatureState(available: Boolean) {
        store.accept(EditorAttachmentsStore.Intent.ChangeFeatureState(available))
    }

    private fun wrap(from: PlatformFile): PlatformFileWrapper =
        PlatformFileWrapper(
            name = from.name,
            extension = from.extension,
            path = from.path.orEmpty(),
            mimeType = FileUtils.getMimeTypeByExtension(from.extension),
            size = from.getSize().orZero(),
            readBytes = from::readBytes,
        )
}
