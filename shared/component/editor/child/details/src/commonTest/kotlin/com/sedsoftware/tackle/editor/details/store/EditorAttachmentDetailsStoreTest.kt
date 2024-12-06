package com.sedsoftware.tackle.editor.details.store

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.editor.details.domain.EditorAttachmentDetailsManager
import com.sedsoftware.tackle.editor.details.model.AttachmentParams
import com.sedsoftware.tackle.editor.details.stubs.EditorAttachmentDetailsApiStub
import com.sedsoftware.tackle.editor.details.Responses
import com.sedsoftware.tackle.utils.test.StoreTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class EditorAttachmentDetailsStoreTest : StoreTest<EditorAttachmentDetailsStore.Intent, EditorAttachmentDetailsStore.State, EditorAttachmentDetailsStore.Label>() {

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    private val attachmentType: MediaAttachmentType = MediaAttachmentType.IMAGE
    private val attachmentUrl: String = "url"
    private val attachmentId: String = "id"
    private val attachmentImageParams: AttachmentParams = AttachmentParams(123, 123, 1f, "blurhash")

    private val api: EditorAttachmentDetailsApiStub = EditorAttachmentDetailsApiStub()
    private val manager: EditorAttachmentDetailsManager = EditorAttachmentDetailsManager(api)

    @Test
    fun `store init should load attachment data`() = runTest {
        // given
        // when
        store.init()
        // then
        assertThat(store.state.initialDescription).isEqualTo(Responses.basicResponse.description)
        assertThat(store.state.description).isEqualTo(Responses.basicResponse.description)

        assertThat(store.state.initialFocus).isEqualTo(
            Responses.basicResponse.meta?.focus?.x to
                Responses.basicResponse.meta?.focus?.y
        )

        assertThat(store.state.focus).isEqualTo(
            Responses.basicResponse.meta?.focus?.x to
                Responses.basicResponse.meta?.focus?.y
        )

        assertThat(store.state.dataChanged).isFalse()
    }

    @Test
    fun `failed store init show show an error message`() = runTest {
        // given
        // when
        api.responseWithException = true
        store.init()
        // then
        assertThat(labels.count { it is EditorAttachmentDetailsStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `OnAlternateTextInput should update state text`() = runTest {
        // given
        val alternateText = "Alt text"
        // when
        store.init()
        store.accept(EditorAttachmentDetailsStore.Intent.OnAlternateTextInput(alternateText))
        // then
        assertThat(store.state.description).isEqualTo(alternateText)
        assertThat(store.state.dataChanged).isTrue()
    }

    @Test
    fun `OnFocusInput should update state focus`() = runTest {
        // given
        val alternateFocus = -0.5f to 0.5f
        // when
        store.init()
        store.accept(EditorAttachmentDetailsStore.Intent.OnFocusInput(alternateFocus.first, alternateFocus.second))
        // then
        assertThat(store.state.focus).isEqualTo(alternateFocus)
        assertThat(store.state.dataChanged).isTrue()
    }

    @Test
    fun `successful SendAttachmentUpdate should send update`() = runTest {
        // given
        val alternateText = "Alt text"
        val alternateFocus = -0.5f to 0.5f
        // when
        store.init()
        store.accept(EditorAttachmentDetailsStore.Intent.OnAlternateTextInput(alternateText))
        store.accept(EditorAttachmentDetailsStore.Intent.OnFocusInput(alternateFocus.first, alternateFocus.second))
        store.accept(EditorAttachmentDetailsStore.Intent.SendAttachmentUpdate)
        // then
        assertThat(labels).contains(EditorAttachmentDetailsStore.Label.AttachmentDataUpdated)
    }

    @Test
    fun `failed SendAttachmentUpdate should show an error`() = runTest {
        // given
        val alternateText = "Alt text"
        val alternateFocus = -0.5f to 0.5f
        // when
        store.init()
        store.accept(EditorAttachmentDetailsStore.Intent.OnAlternateTextInput(alternateText))
        store.accept(EditorAttachmentDetailsStore.Intent.OnFocusInput(alternateFocus.first, alternateFocus.second))
        api.responseWithException = true
        store.accept(EditorAttachmentDetailsStore.Intent.SendAttachmentUpdate)
        // then
        assertThat(labels.count { it is EditorAttachmentDetailsStore.Label.ErrorCaught }).isEqualTo(1)
    }

    override fun createStore():
        Store<EditorAttachmentDetailsStore.Intent, EditorAttachmentDetailsStore.State, EditorAttachmentDetailsStore.Label> =
        EditorAttachmentDetailsStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = manager,
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
        ).create(attachmentType, attachmentUrl, attachmentId, attachmentImageParams, false)
}
