package com.sedsoftware.tackle.editor.details.integration

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent
import com.sedsoftware.tackle.editor.details.model.AttachmentParams
import com.sedsoftware.tackle.editor.details.stubs.EditorAttachmentDetailsApiStub
import com.sedsoftware.tackle.utils.test.ComponentTest
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class EditorAttachmentDetailsComponentTest : ComponentTest<EditorAttachmentDetailsComponentDefault>() {

    private val activeModel: EditorAttachmentDetailsComponent.Model
        get() = component.model.value

    private val attachmentType: MediaAttachmentType = MediaAttachmentType.IMAGE
    private val attachmentUrl: String = "url"
    private val attachmentId: String = "id"
    private val attachmentImageParams: AttachmentParams = AttachmentParams(123, 123, 1f, "blurhash")

    private val api: EditorAttachmentDetailsApiStub = EditorAttachmentDetailsApiStub()

    private var dismissCounter: Int = 0

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `onAttachmentDescriptionInput should update model state`() = runTest {
        // given
        val alternateText = "Alt text"
        // when
        component.onAttachmentDescriptionInput(alternateText)
        // then
        assertThat(activeModel.description).isEqualTo(alternateText)
        assertThat(activeModel.dataChanged).isTrue()
    }

    @Test
    fun `onAttachmentFocusInput should update model focus`() = runTest {
        // given
        val alternateFocus = -0.5f to 0.5f
        // when
        component.onAttachmentFocusInput(alternateFocus.first, alternateFocus.second)
        // then
        assertThat(activeModel.focus).isEqualTo(alternateFocus)
        assertThat(activeModel.dataChanged).isTrue()
    }

    @Test
    fun `onUpdateButtonClicked should send data on success`() = runTest {
        // given
        val alternateText = "Alt text"
        val alternateFocus = -0.5f to 0.5f
        component.onAttachmentDescriptionInput(alternateText)
        component.onAttachmentFocusInput(alternateFocus.first, alternateFocus.second)
        // when
        component.onUpdateButtonClicked()
        // then
        assertThat(componentOutput).contains(ComponentOutput.StatusEditor.AttachmentDataUpdated)
    }

    @Test
    fun `onUpdateButtonClicked should show error message on api error`() = runTest {
        // given
        val alternateText = "Alt text"
        val alternateFocus = -0.5f to 0.5f
        component.onAttachmentDescriptionInput(alternateText)
        component.onAttachmentFocusInput(alternateFocus.first, alternateFocus.second)
        // when
        api.responseWithException = true
        component.onUpdateButtonClicked()
        // then
        assertThat(componentOutput.count { it is ComponentOutput.Common.ErrorCaught }).isGreaterThan(0)
    }

    @Test
    fun `onBackButtonClicked should call for back callback`() = runTest {
        // given
        val currentCounter = dismissCounter
        // when
        component.onBackButtonClicked()
        // then
        assertThat(currentCounter).isNotEqualTo(dismissCounter)
    }

    override fun createComponent(): EditorAttachmentDetailsComponentDefault =
        EditorAttachmentDetailsComponentDefault(
            attachmentType = attachmentType,
            attachmentUrl = attachmentUrl,
            attachmentId = attachmentId,
            attachmentImageParams = attachmentImageParams,
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            api = api,
            dispatchers = testDispatchers,
            output = { componentOutput.add(it) },
            onDismiss = { dismissCounter++ },
        )
}
