package com.sedsoftware.tackle.network.mappers

import assertk.assertThat
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotZero
import assertk.assertions.isTrue
import com.sedsoftware.tackle.domain.model.Instance
import com.sedsoftware.tackle.network.response.InstanceResponse
import com.sedsoftware.tackle.network.responseFromFile
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class InstanceMapperTest {

    @Test
    fun `InstanceMapper should map response to entity`() = runTest {
        // given
        val response: InstanceResponse = responseFromFile("src/commonTest/resources/responses/api_v2_instance.json")
        // when
        val mapped: Instance = InstanceMapper.map(response)
        // then
        assertThat(mapped.domain).isNotEmpty()
        assertThat(mapped.title).isNotEmpty()
        assertThat(mapped.version).isNotEmpty()
        assertThat(mapped.sourceUrl).isNotEmpty()
        assertThat(mapped.description).isNotEmpty()
        assertThat(mapped.activePerMonth).isNotZero()
        assertThat(mapped.thumbnailUrl).isNotEmpty()
        assertThat(mapped.blurhash).isNotEmpty()
        assertThat(mapped.languages).isNotEmpty()
        assertThat(mapped.contactEmail).isNotEmpty()
        assertThat(mapped.contactAccountId).isNotEmpty()
        assertThat(mapped.rules).isNotEmpty()
        assertThat(mapped.rules).isNotEmpty()
        assertThat(mapped.config.accounts.maxFeatureTags).isNotZero()
        assertThat(mapped.config.accounts.maxPinnedStatuses).isNotZero()
        assertThat(mapped.config.mediaAttachments.imageSizeLimit).isNotZero()
        assertThat(mapped.config.mediaAttachments.imageMatrixLimit).isNotZero()
        assertThat(mapped.config.mediaAttachments.videoSizeLimit).isNotZero()
        assertThat(mapped.config.mediaAttachments.videoFrameRateLimit).isNotZero()
        assertThat(mapped.config.mediaAttachments.videoMatrixLimit).isNotZero()
        assertThat(mapped.config.mediaAttachments.supportedMimeTypes).isNotEmpty()
        assertThat(mapped.config.polls.maxOptions).isNotZero()
        assertThat(mapped.config.polls.maxCharactersPerOption).isNotZero()
        assertThat(mapped.config.polls.minExpiration).isNotZero()
        assertThat(mapped.config.polls.maxExpiration).isNotZero()
        assertThat(mapped.config.statuses.maxCharacters).isNotZero()
        assertThat(mapped.config.statuses.maxMediaAttachments).isNotZero()
        assertThat(mapped.config.statuses.charactersReservedPerUrl).isNotZero()
        assertThat(mapped.config.streamingUrl).isNotEmpty()
        assertThat(mapped.config.translationEnabled).isTrue()
    }
}
