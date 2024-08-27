package com.sedsoftware.tackle.editor.attachments.store

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsNone
import assertk.assertions.hasClass
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.editor.attachments.domain.EditorAttachmentsManager
import com.sedsoftware.tackle.editor.attachments.extension.hasPending
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Intent
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Intent.UpdateInstanceConfig
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.Label
import com.sedsoftware.tackle.editor.attachments.store.EditorAttachmentsStore.State
import com.sedsoftware.tackle.editor.attachments.stubs.EditorAttachmentsApiStub
import com.sedsoftware.tackle.editor.attachments.stubs.InstanceConfigStub
import com.sedsoftware.tackle.editor.attachments.stubs.PlatformFileStubs
import com.sedsoftware.tackle.utils.test.StoreTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class EditorAttachmentsStoreTest : StoreTest<Intent, State, Label>() {

    private val api: EditorAttachmentsApiStub = EditorAttachmentsApiStub()
    private val manager: EditorAttachmentsManager = EditorAttachmentsManager(api)

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    @Test
    fun `ChangeFeatureAvailability should change feature availability`() = runTest {
        // given
        // when
        store.init()
        store.accept(Intent.ChangeComponentAvailability(true))
        // then
        assertThat(store.state.attachmentsAvailable).isTrue()
        // and when
        store.accept(Intent.ChangeComponentAvailability(false))
        // then
        assertThat(store.state.attachmentsAvailable).isFalse()
    }

    @Test
    fun `UpdateInstanceConfig should update state`() = runTest {
        // given
        val config = InstanceConfigStub.config
        // when
        store.init()
        store.accept(UpdateInstanceConfig(config))
        // then
        assertThat(store.state.config).isEqualTo(config)
    }

    @Test
    fun `ResetState should reset store state`() = runTest {
        // given
        val files = listOf(
            PlatformFileStubs.imageNormal.copy(name = "test1.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test2.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test3.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test4.jpg"),
        )
        // when
        store.init()
        store.accept(UpdateInstanceConfig(InstanceConfigStub.config))
        store.accept(Intent.OnFilesSelected(files))
        // then
        assertThat(store.state.selectedFiles.size).isEqualTo(files.size)
        assertThat(store.state.attachmentsAtLimit).isTrue()
        assertThat(store.state.selectedFiles.hasPending).isFalse()
        assertThat(labels).contains(Label.PendingAttachmentsCountUpdated(files.size))
        // and when
        store.accept(Intent.ResetState)
        // then
        assertThat(store.state.selectedFiles).isEmpty()
        assertThat(store.state.attachmentsAtLimit).isFalse()
        assertThat(store.state.attachmentsAvailable).isTrue()
        assertThat(store.state.attachmentsVisible).isFalse()
        assertThat(store.state.hasUploadInProgress).isFalse()
    }

    @Test
    fun `four correct files should be loaded ok`() = runTest {
        // given
        val files = listOf(
            PlatformFileStubs.imageNormal.copy(name = "test1.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test2.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test3.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test4.jpg"),
        )
        // when
        store.init()
        store.accept(UpdateInstanceConfig(InstanceConfigStub.config))
        store.accept(Intent.OnFilesSelected(files))
        // then
        assertThat(store.state.selectedFiles.size).isEqualTo(files.size)
        assertThat(store.state.attachmentsAtLimit).isTrue()
        assertThat(store.state.selectedFiles.hasPending).isFalse()
        assertThat(labels).contains(Label.PendingAttachmentsCountUpdated(files.size))
    }

    @Test
    fun `three correct files with one incorrect should produce overall loaded as three with exception`() = runTest {
        // given
        val files = listOf(
            PlatformFileStubs.imageNormal.copy(name = "test1.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test2.jpg"),
            PlatformFileStubs.imageBig.copy(name = "test3.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test4.jpg"),
        )
        // when
        store.init()
        store.accept(UpdateInstanceConfig(InstanceConfigStub.config))
        store.accept(Intent.OnFilesSelected(files))
        // then
        assertThat(store.state.selectedFiles.size).isEqualTo(files.size - 1)
        assertThat(store.state.attachmentsAtLimit).isFalse()
        assertThat(store.state.selectedFiles.hasPending).isFalse()
        assertThat(labels).isNotEmpty()
        assertThat((labels.find { it is Label.ErrorCaught } as Label.ErrorCaught).throwable)
            .hasClass(TackleException.FileSizeExceeded::class)
    }

    @Test
    fun `six correct files should produce overall loaded as four with exception`() = runTest {
        // given
        val files = listOf(
            PlatformFileStubs.imageNormal.copy(name = "test1.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test2.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test3.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test4.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test5.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test6.jpg"),
        )
        // when
        store.init()
        store.accept(UpdateInstanceConfig(InstanceConfigStub.config))
        store.accept(Intent.OnFilesSelected(files))
        // then
        assertThat(store.state.selectedFiles.size).isEqualTo(store.state.config.statuses.maxMediaAttachments)
        assertThat(store.state.attachmentsAtLimit).isTrue()
        assertThat(store.state.selectedFiles.hasPending).isFalse()
        assertThat(labels).isNotEmpty()
        val label = labels.first() as Label.ErrorCaught
        assertThat(label.throwable).hasClass(TackleException.AttachmentsLimitExceeded::class)
    }

    @Test
    fun `four incorrect files should produce overall loaded as zero with exception`() = runTest {
        // given
        val files = listOf(
            PlatformFileStubs.imageBig.copy(name = "test1.jpg"),
            PlatformFileStubs.videoBig.copy(name = "test2.mp4"),
            PlatformFileStubs.fileUnsupported.copy(name = "test3.psd"),
            PlatformFileStubs.fileUnsupported.copy(name = "test4.psd"),
        )
        // when
        store.init()
        store.accept(UpdateInstanceConfig(InstanceConfigStub.config))
        store.accept(Intent.OnFilesSelected(files))
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
            PlatformFileStubs.imageNormal.copy(name = "test1.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test2.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test3.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test4.jpg"),
        )
        api.shouldThrowException = true
        // when
        store.init()
        store.accept(UpdateInstanceConfig(InstanceConfigStub.config))
        store.accept(Intent.OnFilesSelected(files))
        // then
        assertThat(store.state.selectedFiles.count { it.status == AttachedFile.Status.ERROR }).isEqualTo(files.size)
    }

    @Test
    fun `delete should remove target file`() = runTest {
        // given
        val files = listOf(
            PlatformFileStubs.imageNormal.copy(name = "test1.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test2.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test3.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test4.jpg"),
        )
        // when
        store.init()
        store.accept(UpdateInstanceConfig(InstanceConfigStub.config))
        store.accept(Intent.OnFilesSelected(files))
        // given
        val fileToDelete = store.state.selectedFiles[1]
        // when
        store.accept(Intent.OnFileDeleted(fileToDelete.id))
        // then
        assertThat(store.state.selectedFiles).containsNone(fileToDelete)
        assertThat(labels).contains(Label.PendingAttachmentsCountUpdated(files.size - 1))
    }

    @Test
    fun `retry should relaunch download`() = runTest {
        // given
        val files1 = listOf(
            PlatformFileStubs.imageNormal.copy(name = "test1.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test2.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test3.jpg"),
        )

        val files2 = listOf(
            PlatformFileStubs.imageNormal.copy(name = "test4.jpg"),
        )

        // when
        store.init()
        store.accept(UpdateInstanceConfig(InstanceConfigStub.config))
        store.accept(Intent.OnFilesSelected(files1))
        api.shouldThrowException = true
        store.accept(Intent.OnFilesSelected(files2))
        // then
        assertThat(store.state.selectedFiles.count { it.status == AttachedFile.Status.ERROR }).isEqualTo(files2.size)
        // and when
        api.shouldThrowException = false
        val target = store.state.selectedFiles.first { it.status == AttachedFile.Status.ERROR }
        store.accept(Intent.OnFileRetry(target.id))
        // then
        assertThat(store.state.selectedFiles.count { it.status == AttachedFile.Status.ERROR }).isEqualTo(0)
    }

    override fun createStore(): Store<Intent, State, Label> =
        EditorAttachmentsStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = manager,
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
        ).create(autoInit = false)
}
