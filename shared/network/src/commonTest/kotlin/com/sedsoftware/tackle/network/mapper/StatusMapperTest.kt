package com.sedsoftware.tackle.network.mapper

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.network.response.StatusResponse
import com.sedsoftware.tackle.network.responseFromFile
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class StatusMapperTest {

    @Test
    fun `StatusMapper should map status response to entity`() = runTest {
        // given
        val response: StatusResponse = responseFromFile("src/commonTest/resources/responses/api_v1_statuses_normal.json")
        // when
        val mapped: Status = StatusMapper.map(response)
        // then
        with(mapped) {
            assertThat(id).isNotEmpty()
            assertThat(createdAt).isNotNull()
            assertThat(inReplyToId).isEmpty()
            assertThat(inReplyToAccountId).isEmpty()
            assertThat(sensitive).isTrue()
            assertThat(spoilerText).isNotEmpty()
            assertThat(visibility).isEqualTo(StatusVisibility.UNLISTED)
            assertThat(language).isEqualTo("en")
            assertThat(uri).isNotEmpty()
            assertThat(url).isNotEmpty()
            assertThat(repliesCount).isEqualTo(0)
            assertThat(reblogsCount).isEqualTo(0)
            assertThat(favouritesCount).isEqualTo(0)
            assertThat(favourited).isFalse()
            assertThat(reblogged).isFalse()
            assertThat(muted).isFalse()
            assertThat(bookmarked).isFalse()
            assertThat(pinned).isFalse()
            assertThat(content).isNotEmpty()
            assertThat(application!!.name).isNotEmpty()
            assertThat(account).isNotNull()
            assertThat(mediaAttachments).isNotEmpty()
            assertThat(mentions).isNotEmpty()
            assertThat(tags).isNotEmpty()
            assertThat(emojis).isNotEmpty()
            assertThat(poll).isNotNull()
        }
    }
}
