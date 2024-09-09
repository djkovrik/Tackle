package com.sedsoftware.tackle.network.mapper

import assertk.assertThat
import assertk.assertions.isNotEmpty
import com.sedsoftware.tackle.domain.model.Application
import com.sedsoftware.tackle.network.response.ApplicationResponse
import com.sedsoftware.tackle.utils.test.JsonBasedTest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ApplicationMapperTest : JsonBasedTest() {

    @Test
    fun `ApplicationMapper should map response to entity`() = runTest {
        // given
        val response: ApplicationResponse = responseFromFile("src/commonTest/resources/responses/api_v1_apps.json")
        // when
        val mapped: Application = ApplicationMapper.map(response)
        // then
        assertThat(mapped.name).isNotEmpty()
        assertThat(mapped.website).isNotEmpty()
        assertThat(mapped.clientId).isNotEmpty()
        assertThat(mapped.clientSecret).isNotEmpty()
    }
}
