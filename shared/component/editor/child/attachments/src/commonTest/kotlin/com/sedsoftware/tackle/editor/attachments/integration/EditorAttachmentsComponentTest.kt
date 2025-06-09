package com.sedsoftware.tackle.editor.attachments.integration

import assertk.assertThat
import assertk.assertions.containsNone
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsGateways
import com.sedsoftware.tackle.editor.attachments.Instances
import com.sedsoftware.tackle.editor.attachments.Responses
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.utils.test.ComponentTest
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class EditorAttachmentsComponentTest : ComponentTest<EditorAttachmentsComponent>() {

    private val api: EditorAttachmentsGateways.Api = mock {
        everySuspend { sendFile(any(), any(), any()) } returns Responses.sendFileCorrectResponse
    }

    private val activeModel: EditorAttachmentsComponent.Model
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
    fun `onWrappedFilesSelect should update component model`() = runTest {
        // given
        val files = listOf(
            Instances.imageNormal.copy(name = "test1.jpg"),
            Instances.imageNormal.copy(name = "test2.jpg"),
            Instances.imageNormal.copy(name = "test3.jpg"),
            Instances.imageNormal.copy(name = "test4.jpg"),
        )
        val config = Instances.config
        // when
        component.updateInstanceConfig(config)
        component.changeComponentAvailability(available = true)
        component.onWrappedFilesSelect(files)
        // then
        assertThat(activeModel.attachments.size).isEqualTo(files.size)
        assertThat(activeModel.attachmentsButtonAvailable).isFalse()
        assertThat(activeModel.attachmentsContentVisible).isTrue()
    }

    @Test
    fun `onFileDelete should update component model`() = runTest {
        // given
        val files = listOf(
            Instances.imageNormal.copy(name = "test1.jpg"),
            Instances.imageNormal.copy(name = "test2.jpg"),
            Instances.imageNormal.copy(name = "test3.jpg"),
            Instances.imageNormal.copy(name = "test4.jpg"),
        )
        val config = Instances.config
        // when
        component.updateInstanceConfig(config)
        component.changeComponentAvailability(available = true)
        component.onWrappedFilesSelect(files)
        // given
        val fileToDelete = activeModel.attachments[2]
        // when
        component.onFileDelete(fileToDelete.id)
        // then
        assertThat(activeModel.attachments).containsNone(fileToDelete)
        assertThat(activeModel.attachmentsButtonAvailable).isTrue()
    }

    @Test
    fun `onFileRetry should update component model`() = runTest {
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
        val config = Instances.config

        component.updateInstanceConfig(config)
        component.changeComponentAvailability(available = true)
        component.onWrappedFilesSelect(files1)

        everySuspend { api.sendFile(any(), any(), any()) } throws IllegalStateException("Test")
        component.onWrappedFilesSelect(files2)
        // then
        assertThat(activeModel.attachments.count { it.status == AttachedFile.Status.ERROR }).isEqualTo(files2.size)
        // and when
        everySuspend { api.sendFile(any(), any(), any()) } returns Responses.sendFileCorrectResponse
        val target = activeModel.attachments.first { it.status == AttachedFile.Status.ERROR }
        component.onFileRetry(target.id)
        // then
        assertThat(activeModel.attachments.count { it.status == AttachedFile.Status.ERROR }).isEqualTo(0)
    }

    @Test
    fun `changeFeatureAvailability should update feature availability`() = runTest {
        // given
        // when
        component.changeComponentAvailability(false)
        // then
        assertThat(activeModel.attachmentsButtonAvailable).isFalse()
    }

    override fun createComponent(): EditorAttachmentsComponent =
        EditorAttachmentsComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            api = api,
            dispatchers = testDispatchers,
            output = {},
        )
}
