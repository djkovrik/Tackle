package com.sedsoftware.tackle.network.mapper

import assertk.assertThat
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import com.sedsoftware.tackle.domain.model.MediaAttachment
import com.sedsoftware.tackle.network.response.MediaAttachmentResponse
import com.sedsoftware.tackle.network.responseFromFile
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class MediaAttachmentMapperTest {

    @Test
    fun `MediaAttachmentMapper should map response to audio entity`() = runTest {
        // given
        val response: MediaAttachmentResponse = responseFromFile("src/commonTest/resources/responses/api_v2_media_audio.json")
        // when
        val mapped: MediaAttachment = MediaAttachmentMapper.map(response)
        // then
        assertThat(mapped.id).isNotEmpty()
        assertThat(mapped.url).isNotNull()
        assertThat(mapped.previewUrl).isNotNull()
        assertThat(mapped.description).isNotNull()
        assertThat(mapped.meta).isNotNull()
    }


    @Test
    fun `MediaAttachmentMapper should map response to image entity`() = runTest {
        // given
        val response: MediaAttachmentResponse = responseFromFile("src/commonTest/resources/responses/api_v2_media_image.json")
        // when
        val mapped: MediaAttachment = MediaAttachmentMapper.map(response)
        // then
        assertThat(mapped.id).isNotEmpty()
        assertThat(mapped.url).isNotNull()
        assertThat(mapped.previewUrl).isNotEmpty()
        assertThat(mapped.description).isNotNull()
        assertThat(mapped.blurhash).isNotEmpty()
        assertThat(mapped.meta).isNotNull()
    }

    @Test
    fun `MediaAttachmentMapper should map response to video entity`() = runTest {
        // given
        val response: MediaAttachmentResponse = responseFromFile("src/commonTest/resources/responses/api_v2_media_video.json")
        // when
        val mapped: MediaAttachment = MediaAttachmentMapper.map(response)
        // then
        assertThat(mapped.id).isNotEmpty()
        assertThat(mapped.url).isNotNull()
        assertThat(mapped.previewUrl).isNotEmpty()
        assertThat(mapped.description).isNotNull()
        assertThat(mapped.blurhash).isNotEmpty()
        assertThat(mapped.meta).isNotNull()
    }
}
