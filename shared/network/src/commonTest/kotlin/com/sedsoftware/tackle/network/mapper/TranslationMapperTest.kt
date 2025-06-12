package com.sedsoftware.tackle.network.mapper

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import com.sedsoftware.tackle.domain.model.Translation
import com.sedsoftware.tackle.network.response.TranslationResponse
import com.sedsoftware.tackle.utils.test.JsonBasedTest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TranslationMapperTest : JsonBasedTest() {

    @Test
    fun `TranslationMapper should map response to entity`() = runTest {
        // given
        val response: TranslationResponse = responseFromFile("src/commonTest/resources/responses/api_v1_statuses_translate.json")
        // when
        val mapped: Translation = TranslationMapper.map(response)
        // then
        assertThat(mapped.sourceLanguage).isEqualTo("en")
        assertThat(mapped.language).isEqualTo("ru")
        assertThat(mapped.provider).isEqualTo("DeepL.com")
        assertThat(mapped.spoilerText).isEqualTo("Spoiler")
        assertThat(mapped.content).isNotEmpty()
        assertThat(mapped.poll).isNotNull()
        assertThat(mapped.poll?.options?.size).isEqualTo(2)
        assertThat(mapped.attachments.size).isEqualTo(1)
        mapped.poll?.options?.forEach { option ->
            assertThat(option.title).isNotEmpty()
        }
        mapped.attachments.forEach { attachment ->
            assertThat(attachment.id).isNotEmpty()
            assertThat(attachment.description).isNotEmpty()
        }
    }
}
