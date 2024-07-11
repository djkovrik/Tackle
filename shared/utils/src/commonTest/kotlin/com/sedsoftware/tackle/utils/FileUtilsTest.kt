package com.sedsoftware.tackle.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class FileUtilsTest {

    @Test
    fun `getMimeTypeByExtension should return mime type`() {
        // given
        val audio = "ogg"
        val image = "jpg"
        val video = "mp4"
        val unknown = "test"
        // when
        val mimeTypeAudio = FileUtils.getMimeTypeByExtension(audio)
        val mimeTypeImage = FileUtils.getMimeTypeByExtension(image)
        val mimeTypeVideo = FileUtils.getMimeTypeByExtension(video)
        val mimeTypeUnknown = FileUtils.getMimeTypeByExtension(unknown)
        // then
        assertThat(mimeTypeAudio).isEqualTo("audio/ogg")
        assertThat(mimeTypeImage).isEqualTo("image/jpeg")
        assertThat(mimeTypeVideo).isEqualTo("video/mp4")
        assertThat(mimeTypeUnknown).isEqualTo("application/octet-stream")
    }
}
