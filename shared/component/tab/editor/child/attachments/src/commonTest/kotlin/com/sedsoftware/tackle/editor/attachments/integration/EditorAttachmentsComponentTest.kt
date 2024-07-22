package com.sedsoftware.tackle.editor.attachments.integration

import assertk.assertThat
import assertk.assertions.containsNone
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.stubs.EditorAttachmentsApiStub
import com.sedsoftware.tackle.editor.attachments.stubs.InstanceConfigStub
import com.sedsoftware.tackle.editor.attachments.stubs.PlatformFileStubs
import com.sedsoftware.tackle.utils.test.ComponentTest
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class EditorAttachmentsComponentTest : ComponentTest<EditorAttachmentsComponent>() {

    private val activeModel: EditorAttachmentsComponent.Model
        get() = component.model.value

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    @Test
    fun `onFileSelectedWrapped should update component model`() = runTest {
        // given
        val files = listOf(
            PlatformFileStubs.imageNormal.copy(name = "test1.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test2.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test3.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test4.jpg"),
        )
        val config = InstanceConfigStub.config
        // when
        component.updateInstanceConfig(config)
        component.changeComponentAvailability(available = true)
        component.onFilesSelectedWrapped(files)
        // then
        assertThat(activeModel.attachments.size).isEqualTo(files.size)
        assertThat(activeModel.attachmentsButtonAvailable).isFalse()
        assertThat(activeModel.attachmentsContentVisible).isTrue()
    }

    @Test
    fun `onFileDeleted should update component model`() = runTest {
        // given
        val files = listOf(
            PlatformFileStubs.imageNormal.copy(name = "test1.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test2.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test3.jpg"),
            PlatformFileStubs.imageNormal.copy(name = "test4.jpg"),
        )
        val config = InstanceConfigStub.config
        // when
        component.updateInstanceConfig(config)
        component.changeComponentAvailability(available = true)
        component.onFilesSelectedWrapped(files)
        // given
        val fileToDelete = activeModel.attachments[2]
        // when
        component.onFileDeleted(fileToDelete.id)
        // then
        assertThat(activeModel.attachments).containsNone(fileToDelete)
        assertThat(activeModel.attachmentsButtonAvailable).isTrue()
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
            api = EditorAttachmentsApiStub(),
            dispatchers = testDispatchers,
            output = {},
        )
}
