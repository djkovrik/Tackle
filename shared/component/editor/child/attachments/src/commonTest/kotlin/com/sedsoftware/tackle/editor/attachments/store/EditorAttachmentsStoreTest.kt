package com.sedsoftware.tackle.editor.attachments.store

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsNone
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.editor.attachments.Instances
import com.sedsoftware.tackle.editor.attachments.domain.EditorAttachmentsManager
import com.sedsoftware.tackle.editor.attachments.extension.hasPending
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.attachments.stubs.EditorAttachmentsApiStub
import com.sedsoftware.tackle.utils.test.StoreTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class EditorAttachmentsStoreTest :
    StoreTest<EditorAttachmentsStore.Intent, EditorAttachmentsStore.State, EditorAttachmentsStore.Label>() {

    private val api: EditorAttachmentsApiStub = EditorAttachmentsApiStub()
    private val manager: EditorAttachmentsManager = EditorAttachmentsManager(api)

    @Test
    fun `ChangeFeatureAvailability should change feature availability`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorAttachmentsStore.Intent.ChangeComponentAvailability(true))
        // then
        assertThat(store.state.attachmentsAvailable).isTrue()
        // and when
        store.accept(EditorAttachmentsStore.Intent.ChangeComponentAvailability(false))
        // then
        assertThat(store.state.attachmentsAvailable).isFalse()
    }

    @Test
    fun `UpdateInstanceConfig should update state`() = runTest {
        // given
        val config = Instances.config
        // when
        store.init()
        store.accept(EditorAttachmentsStore.Intent.UpdateInstanceConfig(config))
        // then
        assertThat(store.state.config).isEqualTo(config)
    }

    @Test
    fun `four correct files should be loaded ok`() = runTest {
        // given
        val files = listOf(
            Instances.imageNormal.copy(name = "test1.jpg"),
            Instances.imageNormal.copy(name = "test2.jpg"),
            Instances.imageNormal.copy(name = "test3.jpg"),
            Instances.imageNormal.copy(name = "test4.jpg"),
        )
        // when
        store.init()
        store.accept(EditorAttachmentsStore.Intent.UpdateInstanceConfig(Instances.config))
        store.accept(EditorAttachmentsStore.Intent.OnFilesSelected(files))
        // then
        assertThat(store.state.selectedFiles.size).isEqualTo(files.size)
        assertThat(store.state.attachmentsAtLimit).isTrue()
        assertThat(store.state.selectedFiles.hasPending).isFalse()
        assertThat(labels).contains(EditorAttachmentsStore.Label.PendingAttachmentsCountUpdated(files.size))
    }

    @Test
    fun `three correct files with one incorrect should produce overall loaded as three with exception`() = runTest {
        // given
        val files = listOf(
            Instances.imageNormal.copy(name = "test1.jpg"),
            Instances.imageNormal.copy(name = "test2.jpg"),
            Instances.imageBig.copy(name = "test3.jpg"),
            Instances.imageNormal.copy(name = "test4.jpg"),
        )
        // when
        store.init()
        store.accept(EditorAttachmentsStore.Intent.UpdateInstanceConfig(Instances.config))
        store.accept(EditorAttachmentsStore.Intent.OnFilesSelected(files))
        // then
        assertThat(store.state.selectedFiles.size).isEqualTo(files.size - 1)
        assertThat(store.state.attachmentsAtLimit).isFalse()
        assertThat(store.state.selectedFiles.hasPending).isFalse()
        assertThat(labels).isNotEmpty()
        assertThat((labels.find { it is EditorAttachmentsStore.Label.ErrorCaught } as EditorAttachmentsStore.Label.ErrorCaught).throwable)
            .hasClass(TackleException.FileSizeExceeded::class)
    }

    @Test
    fun `six correct files should produce overall loaded as four with exception`() = runTest {
        // given
        val files = listOf(
            Instances.imageNormal.copy(name = "test1.jpg"),
            Instances.imageNormal.copy(name = "test2.jpg"),
            Instances.imageNormal.copy(name = "test3.jpg"),
            Instances.imageNormal.copy(name = "test4.jpg"),
            Instances.imageNormal.copy(name = "test5.jpg"),
            Instances.imageNormal.copy(name = "test6.jpg"),
        )
        // when
        store.init()
        store.accept(EditorAttachmentsStore.Intent.UpdateInstanceConfig(Instances.config))
        store.accept(EditorAttachmentsStore.Intent.OnFilesSelected(files))
        // then
        assertThat(store.state.selectedFiles.size).isEqualTo(store.state.config.statuses.maxMediaAttachments)
        assertThat(store.state.attachmentsAtLimit).isTrue()
        assertThat(store.state.selectedFiles.hasPending).isFalse()
        assertThat(labels).isNotEmpty()
        val label = labels.first() as EditorAttachmentsStore.Label.ErrorCaught
        assertThat(label.throwable).hasClass(TackleException.AttachmentsLimitExceeded::class)
    }

    @Test
    fun `four incorrect files should produce overall loaded as zero with exception`() = runTest {
        // given
        val files = listOf(
            Instances.imageBig.copy(name = "test1.jpg"),
            Instances.videoBig.copy(name = "test2.mp4"),
            Instances.fileUnsupported.copy(name = "test3.psd"),
            Instances.fileUnsupported.copy(name = "test4.psd"),
        )
        // when
        store.init()
        store.accept(EditorAttachmentsStore.Intent.UpdateInstanceConfig(Instances.config))
        store.accept(EditorAttachmentsStore.Intent.OnFilesSelected(files))
        // then
        assertThat(store.state.selectedFiles.size).isEqualTo(0)
        assertThat(store.state.attachmentsAtLimit).isFalse()
        assertThat(store.state.selectedFiles.hasPending).isFalse()
        assertThat(labels).isNotEmpty()
    }

    @Test
    fun `api errors should display related status`() = runTest {
        // given
        val files = listOf(
            Instances.imageNormal.copy(name = "test1.jpg"),
            Instances.imageNormal.copy(name = "test2.jpg"),
            Instances.imageNormal.copy(name = "test3.jpg"),
            Instances.imageNormal.copy(name = "test4.jpg"),
        )
        api.responseWithException = true
        // when
        store.init()
        store.accept(EditorAttachmentsStore.Intent.UpdateInstanceConfig(Instances.config))
        store.accept(EditorAttachmentsStore.Intent.OnFilesSelected(files))
        // then
        assertThat(store.state.selectedFiles.count { it.status == AttachedFile.Status.ERROR }).isEqualTo(files.size)
    }

    @Test
    fun `delete should remove target file`() = runTest {
        // given
        val files = listOf(
            Instances.imageNormal.copy(name = "test1.jpg"),
            Instances.imageNormal.copy(name = "test2.jpg"),
            Instances.imageNormal.copy(name = "test3.jpg"),
            Instances.imageNormal.copy(name = "test4.jpg"),
        )
        // when
        store.init()
        store.accept(EditorAttachmentsStore.Intent.UpdateInstanceConfig(Instances.config))
        store.accept(EditorAttachmentsStore.Intent.OnFilesSelected(files))
        // given
        val fileToDelete = store.state.selectedFiles[1]
        // when
        store.accept(EditorAttachmentsStore.Intent.OnFileDeleted(fileToDelete.id))
        // then
        assertThat(store.state.selectedFiles).containsNone(fileToDelete)
        assertThat(labels).contains(EditorAttachmentsStore.Label.PendingAttachmentsCountUpdated(files.size - 1))
    }

    @Test
    fun `retry should relaunch download`() = runTest {
        // given
        val files1 = listOf(
            Instances.imageNormal.copy(name = "test1.jpg"),
            Instances.imageNormal.copy(name = "test2.jpg"),
            Instances.imageNormal.copy(name = "test3.jpg"),
        )

        val files2 = listOf(
            Instances.imageNormal.copy(name = "test4.jpg"),
        )

        // when
        store.init()
        store.accept(EditorAttachmentsStore.Intent.UpdateInstanceConfig(Instances.config))
        store.accept(EditorAttachmentsStore.Intent.OnFilesSelected(files1))
        api.responseWithException = true
        store.accept(EditorAttachmentsStore.Intent.OnFilesSelected(files2))
        // then
        assertThat(store.state.selectedFiles.count { it.status == AttachedFile.Status.ERROR }).isEqualTo(files2.size)
        // and when
        api.responseWithException = false
        val target = store.state.selectedFiles.first { it.status == AttachedFile.Status.ERROR }
        store.accept(EditorAttachmentsStore.Intent.OnFileRetry(target.id))
        // then
        assertThat(store.state.selectedFiles.count { it.status == AttachedFile.Status.ERROR }).isEqualTo(0)
    }

    @Test
    fun `retry error should show error message`() = runTest {
        // given
        val files1 = listOf(
            Instances.imageNormal.copy(name = "test1.jpg"),
            Instances.imageNormal.copy(name = "test2.jpg"),
            Instances.imageNormal.copy(name = "test3.jpg"),
        )

        val files2 = listOf(
            Instances.imageNormal.copy(name = "test4.jpg"),
        )

        // when
        store.init()
        store.accept(EditorAttachmentsStore.Intent.UpdateInstanceConfig(Instances.config))
        store.accept(EditorAttachmentsStore.Intent.OnFilesSelected(files1))
        api.responseWithException = true
        store.accept(EditorAttachmentsStore.Intent.OnFilesSelected(files2))
        // then
        assertThat(store.state.selectedFiles.count { it.status == AttachedFile.Status.ERROR }).isEqualTo(files2.size)
        // and when
        api.responseWithException = true
        val target = store.state.selectedFiles.first { it.status == AttachedFile.Status.ERROR }
        store.accept(EditorAttachmentsStore.Intent.OnFileRetry(target.id))
        // then
        assertThat(labels.count { it is EditorAttachmentsStore.Label.ErrorCaught }).isEqualTo(2)
    }

    override fun createStore(): Store<EditorAttachmentsStore.Intent, EditorAttachmentsStore.State, EditorAttachmentsStore.Label> =
        EditorAttachmentsStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = manager,
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
        ).create(autoInit = false)
}
