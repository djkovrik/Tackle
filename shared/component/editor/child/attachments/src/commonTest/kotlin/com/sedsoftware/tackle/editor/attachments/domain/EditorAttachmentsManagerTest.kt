package com.sedsoftware.tackle.editor.attachments.domain

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isSameInstanceAs
import assertk.assertions.isTrue
import com.sedsoftware.tackle.domain.TackleException
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.attachments.stubs.EditorAttachmentsApiStub
import com.sedsoftware.tackle.editor.attachments.stubs.EditorAttachmentsApiStubResponses
import com.sedsoftware.tackle.editor.attachments.stubs.InstanceConfigStub
import com.sedsoftware.tackle.editor.attachments.stubs.PlatformFileStubs
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class EditorAttachmentsManagerTest {

    private val api: EditorAttachmentsApiStub = EditorAttachmentsApiStub()
    private val manager: EditorAttachmentsManager = EditorAttachmentsManager(api)

    @Test
    fun `upload should return result on success`() = runTest {
        // given
        val attachment = AttachedFile(
            id = "123",
            file = PlatformFileStubs.imageNormal,
            status = AttachedFile.Status.PENDING,
        )
        api.responseWithException = false
        api.sendFileResponse = EditorAttachmentsApiStubResponses.sendFileCorrectResponse
        // when
        val result = manager.upload(attachment)
        // then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.isFailure).isFalse()
        assertThat(result.getOrThrow().id).isNotEmpty()
    }

    @Test
    fun `upload should catch failure`() = runTest {
        // given
        val attachment = AttachedFile(
            id = "123",
            file = PlatformFileWrapper(
                name = "test.jpg",
                extension = "jpg",
                path = "",
                mimeType = "image/jpeg",
                size = 123L,
                sizeLabel = "123 B",
                readBytes = { ByteArray(0) },
            ),
            status = AttachedFile.Status.PENDING,
        )
        api.responseWithException = true
        api.sendFileResponse = EditorAttachmentsApiStubResponses.sendFileCorrectResponse
        // when
        val result = manager.upload(attachment)
        // then
        assertThat(result.isSuccess).isFalse()
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `prepare with empty file size should throw FileNotAvailable exception`() = runTest {
        // given
        val file = PlatformFileStubs.imageEmpty
        val config = InstanceConfigStub.config
        // when
        val result = manager.prepare(file, config)
        // then
        assertThat(result.isSuccess).isFalse()
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isSameInstanceAs(TackleException.FileNotAvailable)
    }

    @Test
    fun `prepare with unsupported type should throw FileTypeNotSupported exception`() = runTest {
        // given
        val file = PlatformFileStubs.fileUnsupported
        val config = InstanceConfigStub.config
        // when
        val result = manager.prepare(file, config)
        // then
        assertThat(result.isSuccess).isFalse()
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isSameInstanceAs(TackleException.FileTypeNotSupported)
    }

    @Test
    fun `prepare with big image should throw FileSizeExceeded exception`() = runTest {
        // given
        val file = PlatformFileStubs.imageBig
        val config = InstanceConfigStub.config
        // when
        val result = manager.prepare(file, config)
        // then
        assertThat(result.isSuccess).isFalse()
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isSameInstanceAs(TackleException.FileSizeExceeded)
    }

    @Test
    fun `prepare with big video should throw FileSizeExceeded exception`() = runTest {
        // given
        val file = PlatformFileStubs.videoBig
        val config = InstanceConfigStub.config
        // when
        val result = manager.prepare(file, config)
        // then
        assertThat(result.isSuccess).isFalse()
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isSameInstanceAs(TackleException.FileSizeExceeded)
    }

    @Test
    fun `prepare with normal file should return AttachedFile`() = runTest {
        // given
        val file1 = PlatformFileStubs.imageNormal
        val file2 = PlatformFileStubs.videoNormal
        val config = InstanceConfigStub.config
        // when
        val result1 = manager.prepare(file1, config)
        val result2 = manager.prepare(file2, config)
        // then
        assertThat(result1.isSuccess).isTrue()
        assertThat(result2.isSuccess).isTrue()
        assertThat(result1.getOrThrow().id).isNotEmpty()
        assertThat(result2.getOrThrow().id).isNotEmpty()
    }
}
