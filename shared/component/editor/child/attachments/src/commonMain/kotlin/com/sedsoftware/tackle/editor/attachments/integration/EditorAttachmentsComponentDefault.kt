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
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent.Model
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsGateways
import com.sedsoftware.tackle.editor.attachments.domain.EditorAttachmentsManager
import com.sedsoftware.tackle.editor.attachments.extension.wrap
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Label
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStoreProvider
import com.sedsoftware.tackle.utils.extension.asValue
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class EditorAttachmentsComponentDefault(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val api: EditorAttachmentsGateways.Api,
    private val dispatchers: TackleDispatchers,
    private val output: (ComponentOutput) -> Unit,
) : EditorAttachmentsComponent, ComponentContext by componentContext {

    private val store: EditorAttachmentsStore =
        instanceKeeper.getStore {
            EditorAttachmentsStoreProvider(
                storeFactory = storeFactory,
                manager = EditorAttachmentsManager(api),
                mainContext = dispatchers.main,
                ioContext = dispatchers.io,
            ).create()
        }

    init {
        val scope = CoroutineScope(dispatchers.main)

        scope.launch {
            store.labels.collect { label ->
                when (label) {
                    is Label.PendingAttachmentsCountUpdated ->
                        output(ComponentOutput.StatusEditor.PendingAttachmentsCountUpdated(label.count))
                    is Label.LoadedAttachmentsCountUpdated ->
                        output(ComponentOutput.StatusEditor.LoadedAttachmentsCountUpdated(label.count))
                    is Label.ErrorCaught ->
                        output(ComponentOutput.Common.ErrorCaught(label.throwable))
                }
            }
        }

        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }

    override val model: Value<Model> = store.asValue().map(stateToModel)

    override fun onFilesSelect(files: List<PlatformFile>) {
        val wrapped: List<PlatformFileWrapper> = files.map { wrap(it.name, it.extension, it.path, it::size, it::readBytes) }
        if (wrapped.isNotEmpty()) {
            store.accept(EditorAttachmentsStore.Intent.OnFilesSelected(wrapped))
        }
    }

    override fun onWrappedFilesSelect(files: List<PlatformFileWrapper>) {
        if (files.isNotEmpty()) {
            store.accept(EditorAttachmentsStore.Intent.OnFilesSelected(files))
        }
    }

    override fun onFileDelete(id: String) {
        store.accept(EditorAttachmentsStore.Intent.OnFileDeleted(id))
    }

    override fun onFileRetry(id: String) {
        store.accept(EditorAttachmentsStore.Intent.OnFileRetryClicked(id))
    }

    override fun onFileEdit(attachment: MediaAttachment) {
        output(ComponentOutput.StatusEditor.AttachmentDetailsRequested(attachment))
    }

    override fun changeComponentAvailability(available: Boolean) {
        store.accept(EditorAttachmentsStore.Intent.ChangeComponentAvailability(available))
    }

    override fun updateInstanceConfig(config: Instance.Config) {
        store.accept(EditorAttachmentsStore.Intent.UpdateInstanceConfig(config))
    }
}
