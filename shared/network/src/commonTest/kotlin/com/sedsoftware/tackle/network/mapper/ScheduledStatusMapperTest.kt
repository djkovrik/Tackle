package com.sedsoftware.tackle.network.mapper

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isNotZero
import assertk.assertions.isTrue
import com.sedsoftware.tackle.domain.model.ScheduledStatus
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.network.response.ScheduledStatusResponse
import com.sedsoftware.tackle.network.responseFromFile
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ScheduledStatusMapperTest {

    @Test
    fun `ScheduledStatusMapper should map scheduled status response to entity`() = runTest {
        // given
        val response: ScheduledStatusResponse = responseFromFile("src/commonTest/resources/responses/api_v1_statuses_scheduled.json")
        // when
        val mapped: ScheduledStatus = ScheduledStatusMapper.map(response)
        // then
        with(mapped) {
            assertThat(id).isNotEmpty()
            assertThat(scheduledAt).isNotNull()
            assertThat(params).isNotNull()
            assertThat(params.text).isNotNull()
            assertThat(params.language).isEqualTo("en")
            assertThat(params.visibility).isEqualTo(StatusVisibility.PUBLIC)
            assertThat(params.poll).isNotNull()
            assertThat(params.poll!!.multiple).isTrue()
            assertThat(params.poll!!.hideTotals).isTrue()
            assertThat(params.poll!!.options).isNotEmpty()
            assertThat(params.applicationId).isNotZero()
            assertThat(mediaAttachments).isNotEmpty()
        }
    }
}
