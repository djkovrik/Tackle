package com.sedsoftware.tackle.editor.details.domain

import assertk.assertThat
import assertk.assertions.isTrue
import com.sedsoftware.tackle.editor.details.stubs.EditorAttachmentDetailsApiStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class EditorAttachmentDetailsManagerTest {

    private val api: EditorAttachmentDetailsApiStub = EditorAttachmentDetailsApiStub()
    private val manager: EditorAttachmentDetailsManager = EditorAttachmentDetailsManager(api)

    @Test
    fun `updateFile should finish successfully`() = runTest {
        // given
        val id = "id"
        val description = "description"
        val focus = 1f to 1f
        // when
        val result = manager.updateFile(id, description, focus)
        // then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `updateFile should throw an error on api error`() = runTest {
        // given
        val id = "id"
        val description = "description"
        val focus = 1f to 1f
        // when
        api.responseWithException = true
        val result = manager.updateFile(id, description, focus)
        // then
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `getFile should return file successfully`() = runTest {
        // given
        val id = "id"
        // when
        val result = manager.getFile(id)
        // then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `getFile should throw an error on api error`() = runTest {
        // given
        val id = "id"
        // when
        api.responseWithException = true
        val result = manager.getFile(id)
        // then
        assertThat(result.isFailure).isTrue()
    }
}
