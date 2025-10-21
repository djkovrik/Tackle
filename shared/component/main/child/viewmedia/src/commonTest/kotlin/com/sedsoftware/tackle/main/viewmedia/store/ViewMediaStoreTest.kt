package com.sedsoftware.tackle.main.viewmedia.store

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponentGateways
import com.sedsoftware.tackle.main.viewmedia.domain.FileDownloadManager
import com.sedsoftware.tackle.utils.test.StoreTest
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ViewMediaStoreTest : StoreTest<ViewMediaStore.Intent, ViewMediaStore.State, ViewMediaStore.Label>() {

    private val api: ViewMediaComponentGateways.Api = mock()
    private val manager: FileDownloadManager = FileDownloadManager(api)
    private val attachments: List<MediaAttachment> = listOf(getAttachment("url1"), getAttachment("url2"))
    private val initialIndex: Int = 0

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `store init should fill state`() = runTest {
        // given
        // when
        store.init()
        // then
        assertThat(store.state.attachments).isEqualTo(attachments)
        assertThat(store.state.selectedIndex).isEqualTo(initialIndex)
        assertThat(store.state.downloadInProgress.size).isEqualTo(attachments.size)
        assertThat(store.state.downloadProgress.size).isEqualTo(attachments.size)
        assertThat(store.state.downloadCompleted.size).isEqualTo(attachments.size)

        assertThat(store.state.downloadInProgress.count { it == false }).isEqualTo(attachments.size)
        assertThat(store.state.downloadProgress.count { it == 0f }).isEqualTo(attachments.size)
        assertThat(store.state.downloadCompleted.count { it == false }).isEqualTo(attachments.size)
    }

    @Test
    fun `OnSelectionChanged should update current selection state`() = runTest {
        // given
        val newIndex = 1
        store.init()
        // when
        store.accept(ViewMediaStore.Intent.OnSelectionChanged(newIndex))
        // then
        assertThat(store.state.selectedIndex).isEqualTo(newIndex)
    }

    @Test
    fun `OnDownloadClicked should update downloads state`() = runTest {
        // given
        val url1 = "url1"
        val destination1 = PlatformFile("test1")
        val downloadFlow1: MutableStateFlow<Float> = MutableStateFlow(0f)
        everySuspend { api.downloadFile(url1, any()) } returns downloadFlow1

        val url2 = "url2"
        val destination2 = PlatformFile("test2")
        val downloadFlow2: MutableStateFlow<Float> = MutableStateFlow(0f)
        everySuspend { api.downloadFile(url2, any()) } returns downloadFlow2

        val tempProgress = 0.5f
        store.init()

        // when download selection 0
        store.accept(ViewMediaStore.Intent.OnDownloadClicked(destination1))
        downloadFlow1.tryEmit(tempProgress)
        // then
        assertThat(store.state.downloadInProgress[store.state.selectedIndex]).isTrue()
        assertThat(store.state.downloadInProgress.count { it }).isEqualTo(1)
        assertThat(store.state.downloadProgress[store.state.selectedIndex]).isEqualTo(tempProgress)
        assertThat(store.state.downloadProgress.count { it == tempProgress }).isEqualTo(1)
        assertThat(store.state.downloadCompleted.count { it }).isEqualTo(0)
        // when switch to selection 1 and download
        store.accept(ViewMediaStore.Intent.OnSelectionChanged(1))
        store.accept(ViewMediaStore.Intent.OnDownloadClicked(destination2))
        downloadFlow2.tryEmit(tempProgress)
        // then
        assertThat(store.state.selectedIndex).isEqualTo(1)
        assertThat(store.state.downloadInProgress[store.state.selectedIndex]).isTrue()
        assertThat(store.state.downloadInProgress.count { it }).isEqualTo(2)
        assertThat(store.state.downloadProgress[store.state.selectedIndex]).isEqualTo(tempProgress)
        assertThat(store.state.downloadProgress.count { it == tempProgress }).isEqualTo(2)
        assertThat(store.state.downloadCompleted.count { it }).isEqualTo(0)
        // when selection 0 completed
        downloadFlow1.tryEmit(1f)
        // then
        assertThat(store.state.downloadInProgress[0]).isFalse()
        assertThat(store.state.downloadInProgress.count { it }).isEqualTo(1)
        assertThat(store.state.downloadCompleted.count { it }).isEqualTo(1)
        // when selection 1 completed
        downloadFlow2.tryEmit(1f)
        // then
        assertThat(store.state.downloadInProgress[1]).isFalse()
        assertThat(store.state.downloadInProgress.count { it }).isEqualTo(0)
        assertThat(store.state.downloadCompleted.count { it }).isEqualTo(2)
    }

    @Test
    fun `download failure should reset state`() = runTest {
        // given
        val errorValue = 123f
        val internalStateFlow: MutableStateFlow<Float> = MutableStateFlow(0f)
        val downloadFlow: Flow<Float> = flow {
            internalStateFlow
                .map { value -> if (value == errorValue) error("Test failure") else value }
                .collect { value -> emit(value) }
        }
        val url = "url1"
        val destination = PlatformFile("test2")
        everySuspend { api.downloadFile(url, any()) } returns downloadFlow

        val tempProgress = 0.5f
        store.init()

        // when
        store.accept(ViewMediaStore.Intent.OnDownloadClicked(destination))
        internalStateFlow.value = tempProgress

        // then
        assertThat(store.state.downloadInProgress[store.state.selectedIndex]).isTrue()
        assertThat(store.state.downloadInProgress.count { it }).isEqualTo(1)
        assertThat(store.state.downloadProgress[store.state.selectedIndex]).isEqualTo(tempProgress)
        assertThat(store.state.downloadProgress.count { it == tempProgress }).isEqualTo(1)
        assertThat(store.state.downloadCompleted.count { it }).isEqualTo(0)

        // when
        internalStateFlow.value = errorValue

        // then
        assertThat(store.state.downloadInProgress.count { it }).isEqualTo(0)
        assertThat(store.state.downloadProgress.count { it != 0f }).isEqualTo(0)
        assertThat(store.state.downloadCompleted.count { it }).isEqualTo(0)

        assertThat(labels.size).isNotEqualTo(0)
    }

    override fun createStore(): Store<ViewMediaStore.Intent, ViewMediaStore.State, ViewMediaStore.Label> =
        ViewMediaStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = manager,
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
        ).create(
            attachments = attachments,
            selectedIndex = initialIndex,
            autoInit = false,
        )

    private fun getAttachment(url: String): MediaAttachment =
        MediaAttachment(
            id = Random.nextInt(1000).toString(),
            type = MediaAttachmentType.entries.toTypedArray().take(5).random(),
            url = url,
            previewUrl = Random.nextInt(1000).toString(),
            remoteUrl = Random.nextInt(1000).toString(),
            description = Random.nextInt(1000).toString(),
            blurhash = Random.nextInt(1000).toString(),
            meta = null,
        )
}
