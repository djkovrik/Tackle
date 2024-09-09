package com.sedsoftware.tackle.network.mapper

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.network.response.AccountResponse
import com.sedsoftware.tackle.utils.test.JsonBasedTest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class AccountMapperTest : JsonBasedTest() {

    @Test
    fun `AccountMapper should map response to entity`() = runTest {
        // given
        val response: AccountResponse = responseFromFile("src/commonTest/resources/responses/api_v1_accounts_verify_credentials.json")
        // when
        val mapped: Account = AccountMapper.map(response)
        // then
        assertThat(mapped.id).isNotEmpty()
        assertThat(mapped.username).isNotEmpty()
        assertThat(mapped.acct).isNotEmpty()
        assertThat(mapped.displayName).isNotNull()
        assertThat(mapped.note).isNotNull()
        assertThat(mapped.url).isNotEmpty()
        assertThat(mapped.uri).isNotEmpty()
        assertThat(mapped.avatar).isNotEmpty()
        assertThat(mapped.avatarStatic).isNotEmpty()
        assertThat(mapped.header).isNotEmpty()
        assertThat(mapped.headerStatic).isNotEmpty()
        assertThat(mapped.locked).isFalse()
        assertThat(mapped.noIndex).isFalse()
    }
}
