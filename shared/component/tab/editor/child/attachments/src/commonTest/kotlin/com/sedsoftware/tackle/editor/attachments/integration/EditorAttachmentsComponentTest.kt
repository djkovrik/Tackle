package com.sedsoftware.tackle.editor.attachments.integration

import assertk.assertThat
import assertk.assertions.isFalse
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.stubs.EditorAttachmentsApiStub
import com.sedsoftware.tackle.utils.test.ComponentTest
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class EditorAttachmentsComponentTest : ComponentTest<EditorAttachmentsComponentDefault>() {

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
    fun `changeFeatureState should update feature state`() = runTest {
        // given
        // when
        component.changeFeatureState(false)
        // then
        assertThat(activeModel.attachmentButtonAvailable).isFalse()
    }

    override fun createComponent(): EditorAttachmentsComponentDefault =
        EditorAttachmentsComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            api = EditorAttachmentsApiStub(),
            dispatchers = testDispatchers,
            output = {},
        )
}
