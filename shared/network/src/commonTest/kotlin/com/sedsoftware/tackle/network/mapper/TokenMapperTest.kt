package com.sedsoftware.tackle.network.mapper

import assertk.assertThat
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotZero
import com.sedsoftware.tackle.domain.model.Token
import com.sedsoftware.tackle.network.response.TokenResponse
import com.sedsoftware.tackle.utils.test.JsonBasedTest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TokenMapperTest : JsonBasedTest() {

    @Test
    fun `TokenMapper should map response to entity`() = runTest {
        // given
        val response: TokenResponse = responseFromFile("src/commonTest/resources/responses/oauth_token.json")
        // when
        val mapped: Token = TokenMapper.map(response)
        // then
        assertThat(mapped.accessToken).isNotEmpty()
        assertThat(mapped.tokenType).isNotEmpty()
        assertThat(mapped.scope).isNotEmpty()
        assertThat(mapped.createdAt).isNotZero()
    }
}
