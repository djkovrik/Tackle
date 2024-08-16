package com.sedsoftware.tackle.network.mapper

import assertk.assertThat
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.network.response.CustomEmojiResponse
import com.sedsoftware.tackle.network.responseFromFile
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class CustomEmojiMapperTest {

    @Test
    fun `CustomEmojiMapper should map response to entities`() = runTest {
        // given
        val response: List<CustomEmojiResponse> = responseFromFile("src/commonTest/resources/responses/api_v1_custom_emojis.json")
        // when
        val mapped: List<CustomEmoji> = CustomEmojiMapper.map(response)
        // then
        assertThat(mapped.isNotEmpty())
        mapped.forEach { emoji ->
            assertThat(emoji.shortcode).isNotEmpty()
            assertThat(emoji.url).isNotEmpty()
            assertThat(emoji.staticUrl).isNotEmpty()
            assertThat(emoji.category).isNotNull()
            assertThat(emoji.visibleInPicker).isNotNull()
        }
    }
}
