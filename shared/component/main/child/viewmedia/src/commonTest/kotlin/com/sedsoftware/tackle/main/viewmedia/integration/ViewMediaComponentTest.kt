package com.sedsoftware.tackle.main.viewmedia.integration

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponent
import com.sedsoftware.tackle.main.viewmedia.ViewMediaComponentGateways
import com.sedsoftware.tackle.utils.test.ComponentTest
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.exactly
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ViewMediaComponentTest : ComponentTest<ViewMediaComponent>() {

    private val onBackMock: () -> Unit = mock {
        every { invoke() } returns Unit
    }
    private val downloadsFlow: MutableStateFlow<Float> = MutableStateFlow(0f)
    private val api: ViewMediaComponentGateways.Api = mock {
        everySuspend { downloadFile(any(), any()) } returns downloadsFlow
    }

    private val activeModel: ViewMediaComponent.Model
        get() = component.model.value

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `onBack should call for back callback`() = runTest {
        // given
        // when
        component.onBack()
        // then
        verify(exactly(1)) { onBackMock() }
    }

    @Test
    fun `onSelectNewItem should update model`() = runTest {
        // given
        val newIndex = 12
        // when
        component.onSelectNewItem(newIndex)
        // then
        assertThat(activeModel.selectedIndex).isEqualTo(newIndex)
    }

    @Test
    fun `onDownload should update download progress`() = runTest {
        // given
        val tempProgress = 0.5f
        val destination = PlatformFile("test")
        // when
        component.onDownload(destination)
        downloadsFlow.tryEmit(tempProgress)
        // then
        assertThat(activeModel.selectedIndex).isEqualTo(0)
        assertThat(activeModel.downloadInProgress[0]).isTrue()
        assertThat(activeModel.downloadCompleted[0]).isFalse()
        // when
        downloadsFlow.tryEmit(1f)
        assertThat(activeModel.downloadInProgress[0]).isFalse()
        assertThat(activeModel.downloadCompleted[0]).isTrue()
    }


    override fun createComponent(): ViewMediaComponent =
        ViewMediaComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            api = api,
            attachments = listOf(MediaAttachment("url", MediaAttachmentType.AUDIO, "", "", "", "", "", null)),
            selectedIndex = 0,
            onBackClicked = onBackMock,
            dispatchers = testDispatchers,
            viewMediaOutput = {},
        )
}
