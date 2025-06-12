package com.sedsoftware.tackle.editor.details.domain

import assertk.assertThat
import assertk.assertions.isTrue
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsGateways
import com.sedsoftware.tackle.editor.details.Responses
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class EditorAttachmentDetailsManagerTest {

    private val api: EditorAttachmentDetailsGateways.Api = mock {
        everySuspend { getFile(any()) } returns Responses.basicResponse
        everySuspend { updateFile(any(), any(), any()) } returns Responses.basicResponse
    }

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
        everySuspend { api.updateFile(any(), any(), any()) } throws IllegalStateException("Test")
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
        everySuspend { api.getFile(any()) } throws IllegalStateException("Test")
        val result = manager.getFile(id)
        // then
        assertThat(result.isFailure).isTrue()
    }
}
