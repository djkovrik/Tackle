package com.sedsoftware.tackle.editor.attachments.extension

import assertk.assertThat
import assertk.assertions.containsNone
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType.IMAGE
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile.Status.ERROR
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile.Status.LOADED
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile.Status.LOADING
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile.Status.PENDING
import com.sedsoftware.tackle.editor.attachments.model.UploadProgress
import com.sedsoftware.tackle.editor.attachments.stubs.PlatformFileStubs
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ExtensionsTest {

    private val attachedFileStub: AttachedFile =
        AttachedFile(
            id = "123",
            file = PlatformFileStubs.imageNormal,
            status = PENDING,
            uploadProgress = 12,
            serverCopy = null,
        )

    @Test
    fun `hasPending should check pending`() = runTest {
        // given
        val listWithPending = listOf(
            attachedFileStub.copy(id = "1", status = LOADING),
            attachedFileStub.copy(id = "2", status = ERROR),
            attachedFileStub.copy(id = "3", status = PENDING),
            attachedFileStub.copy(id = "4", status = LOADED),
        )
        val listWithNoPending = listOf(
            attachedFileStub.copy(id = "1", status = LOADING),
            attachedFileStub.copy(id = "2", status = ERROR),
            attachedFileStub.copy(id = "3", status = ERROR),
            attachedFileStub.copy(id = "4", status = LOADED),
        )
        // when
        // then
        assertThat(listWithPending.hasPending).isTrue()
        assertThat(listWithNoPending.hasPending).isFalse()
    }

    @Test
    fun `hasActiveUpload should check loading`() = runTest {
        // given
        val listWithUpload = listOf(
            attachedFileStub.copy(id = "1", status = PENDING),
            attachedFileStub.copy(id = "2", status = ERROR),
            attachedFileStub.copy(id = "3", status = LOADING),
            attachedFileStub.copy(id = "4", status = LOADED),
        )
        val listWithNoUpload = listOf(
            attachedFileStub.copy(id = "1", status = PENDING),
            attachedFileStub.copy(id = "2", status = ERROR),
            attachedFileStub.copy(id = "3", status = ERROR),
            attachedFileStub.copy(id = "4", status = LOADED),
        )
        // when
        // then
        assertThat(listWithUpload.hasActiveUpload).isTrue()
        assertThat(listWithNoUpload.hasActiveUpload).isFalse()
    }

    @Test
    fun `firstPending should return correct file`() = runTest {
        // given
        val list = listOf(
            attachedFileStub.copy(id = "1", status = LOADING),
            attachedFileStub.copy(id = "2", status = ERROR),
            attachedFileStub.copy(id = "3", status = PENDING),
            attachedFileStub.copy(id = "4", status = LOADED),
        )
        // when
        val item = list.firstPending
        // then
        assertThat(item).isEqualTo(list[2])
    }

    @Test
    fun `updateStatus should update list item`() = runTest {
        // given
        val list = listOf(
            attachedFileStub.copy(id = "1", status = LOADING),
            attachedFileStub.copy(id = "2", status = ERROR),
            attachedFileStub.copy(id = "3", status = PENDING),
            attachedFileStub.copy(id = "4", status = LOADING),
        )
        val targetItemIndex = 2
        // when
        val newList = list.updateStatus(list[targetItemIndex].id, LOADED)
        // then
        assertThat(newList[targetItemIndex].status).isEqualTo(LOADED)
    }

    @Test
    fun `updateServerCopy should update list item`() = runTest {
        // given
        val list = listOf(
            attachedFileStub.copy(id = "1", status = LOADING),
            attachedFileStub.copy(id = "2", status = ERROR),
            attachedFileStub.copy(id = "3", status = PENDING),
            attachedFileStub.copy(id = "4", status = LOADING),
        )
        val targetItemIndex = 1
        val serverCopy = MediaAttachment(
            id = "111",
            type = IMAGE,
            url = "url",
            previewUrl = "url",
            remoteUrl = "url",
            description = "desc",
            blurhash = "123",
            meta = null,
        )
        // when
        val newList = list.updateServerCopy(list[targetItemIndex].id, serverCopy)
        // then
        assertThat(newList[targetItemIndex].serverCopy).isEqualTo(serverCopy)
    }

    @Test
    fun `updateProgress should update progress`() = runTest {
        // given
        val list = listOf(
            attachedFileStub.copy(id = "1", status = LOADING),
            attachedFileStub.copy(id = "2", status = ERROR),
            attachedFileStub.copy(id = "3", status = PENDING),
            attachedFileStub.copy(id = "4", status = LOADING),
        )
        val targetItemIndex = 2
        val progress = 75
        // when
        val newList = list.updateProgress(UploadProgress(list[targetItemIndex].id, progress))
        // then
        assertThat(newList[targetItemIndex].uploadProgress).isEqualTo(progress)
    }

    @Test
    fun `delete should remove file from the list`() = runTest {
        // given
        val list = listOf(
            attachedFileStub.copy(id = "1", status = LOADING),
            attachedFileStub.copy(id = "2", status = ERROR),
            attachedFileStub.copy(id = "3", status = PENDING),
            attachedFileStub.copy(id = "4", status = LOADING),
        )
        val targetItemIndex = 2
        val targetItem = list[targetItemIndex]
        // when
        val newList = list.delete(targetItem.id)
        // then
        assertThat(newList).containsNone(targetItem)
    }

    @Test
    fun `get should return correct file`() = runTest {
        // given
        val list = listOf(
            attachedFileStub.copy(id = "11", status = LOADING),
            attachedFileStub.copy(id = "22", status = ERROR),
            attachedFileStub.copy(id = "33", status = PENDING),
            attachedFileStub.copy(id = "44", status = LOADING),
        )
        // when
        val target = list.getById(id = "33")
        // then
        assertThat(target).isEqualTo(list[2])
    }

    @Test
    fun `wrap should create PlatformWrapper`() = runTest {
        // given
        val name = "file.mp4"
        val extension = "mp4"
        val path = "pa/th"
        val getSize = { 123L }
        val readBytes = { ByteArray(0) }
        // when
        val wrapped = wrap(name, extension, path, getSize, readBytes)
        // then
        assertThat(wrapped.name).isEqualTo(name)
        assertThat(wrapped.extension).isEqualTo(extension)
        assertThat(wrapped.path).isEqualTo(path)
        assertThat(wrapped.size).isEqualTo(getSize.invoke())
        assertThat(wrapped.sizeLabel).isEqualTo("123.0 b")
        assertThat(wrapped.mimeType).isEqualTo("video/mp4")
    }
}
