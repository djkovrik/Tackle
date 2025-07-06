package com.sedsoftware.tackle.network.mapper

import assertk.assertThat
import assertk.assertions.endsWith
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotEqualTo
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.MediaAttachmentType
import com.sedsoftware.tackle.network.response.StatusResponse
import com.sedsoftware.tackle.utils.test.JsonBasedTest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class MediaContentTest : JsonBasedTest() {

    @Test
    fun `StatusListMapper should map status list response to entity`() = runTest {
        // given
        val response: List<StatusResponse> = responseFromFile("src/commonTest/resources/responses/media_content.json")
        // when
        val mapped: List<Status> = StatusListMapper.map(response)
        // then
        with(mapped[0].mediaAttachments.first()) {
            assertThat(type).isEqualTo(MediaAttachmentType.AUDIO)
            assertThat(url).endsWith("mp3")
            assertThat(previewUrl).isEmpty()
            assertThat(description).isNotEmpty()
            assertThat(blurhash).isEmpty()
            assertThat(meta?.original?.duration).isNotEqualTo(0)
            assertThat(meta?.original?.bitrate).isNotEqualTo(0)
        }

        with(mapped[1].mediaAttachments.first()) {
            assertThat(type).isEqualTo(MediaAttachmentType.VIDEO)
            assertThat(url).endsWith("mp4")
            assertThat(previewUrl).endsWith("png")
            assertThat(description).isNotEmpty()
            assertThat(blurhash).isNotEmpty()
            assertThat(meta?.original?.width).isEqualTo(1280)
            assertThat(meta?.original?.height).isEqualTo(720)
            assertThat(meta?.small?.width).isEqualTo(640)
            assertThat(meta?.small?.height).isEqualTo(360)
            assertThat(meta?.small?.aspect).isNotEqualTo(0f)
        }

        with(mapped[2].mediaAttachments.first()) {
            assertThat(type).isEqualTo(MediaAttachmentType.GIFV)
            assertThat(url).endsWith("mp4")
            assertThat(previewUrl).endsWith("png")
            assertThat(description).isNotEmpty()
            assertThat(blurhash).isNotEmpty()
            assertThat(meta?.original?.width).isEqualTo(400)
            assertThat(meta?.original?.height).isEqualTo(200)
            assertThat(meta?.small?.width).isEqualTo(400)
            assertThat(meta?.small?.height).isEqualTo(200)
            assertThat(meta?.small?.aspect).isEqualTo(2.0f)
        }

        with(mapped[3].mediaAttachments.first()) {
            assertThat(type).isEqualTo(MediaAttachmentType.IMAGE)
            assertThat(url).endsWith("png")
            assertThat(previewUrl).endsWith("png")
            assertThat(description).isNotEmpty()
            assertThat(blurhash).isNotEmpty()
            assertThat(meta?.original?.width).isEqualTo(2560)
            assertThat(meta?.original?.height).isEqualTo(1440)
            assertThat(meta?.original?.aspect).isEqualTo(1.7777778f)
            assertThat(meta?.original?.size).isEqualTo("2560x1440")
            assertThat(meta?.small?.width).isEqualTo(640)
            assertThat(meta?.small?.height).isEqualTo(360)
            assertThat(meta?.small?.aspect).isEqualTo(1.7777778f)
            assertThat(meta?.small?.size).isEqualTo("640x360")
            assertThat(meta?.focus?.x).isEqualTo(0.19f)
            assertThat(meta?.focus?.y).isEqualTo(0.5f)
        }
    }
}
