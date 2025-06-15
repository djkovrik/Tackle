package com.sedsoftware.tackle.network.mapper

import assertk.assertThat
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.network.response.StatusResponse
import com.sedsoftware.tackle.utils.test.JsonBasedTest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class StatusListMapperTest : JsonBasedTest() {

    @Test
    fun `StatusListMapper should map status list response to entity`() = runTest {
        // given
        val response: List<StatusResponse> = responseFromFile("src/commonTest/resources/responses/api_v1_timelines_home.json")
        // when
        val mapped: List<Status> = StatusListMapper.map(response)
        // then
        mapped.forEach { status ->
            assertThat(status.id).isNotEmpty()
            assertThat(status.createdAt).isNotNull()
        }
    }
}
