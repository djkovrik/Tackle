package com.sedsoftware.tackle.statuslist.domain

import assertk.assertThat
import assertk.assertions.isTrue
import com.sedsoftware.tackle.domain.model.type.Timeline
import com.sedsoftware.tackle.status.StatusComponentGateways
import com.sedsoftware.tackle.statuslist.Responses
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class StatusListManagerTest {

    private val api: StatusComponentGateways.Api = mock {
        everySuspend { homeTimeline(any()) } returns listOf(Responses.status)
        everySuspend { publicTimeline(any(), any()) } returns listOf(Responses.status)
        everySuspend { hashTagTimeline(any(), any(), any()) } returns listOf(Responses.status)
        everySuspend { listTimeline(any(), any()) } returns listOf(Responses.status)
    }

    @Test
    fun `loadStatusList for home timeline calls for api`() = runTest {
        // given
        val timeline = Timeline.Home
        val manager = StatusListManager(timeline, api)
        val pageSize = 20
        val maxId = "maxId"
        // when
        val result = manager.loadStatusList(pageSize, maxId)
        // then
        assertThat(result.isSuccess).isTrue()
        verifySuspend(exactly(1)) { api.homeTimeline(any()) }
    }

    @Test
    fun `loadStatusList for public timeline calls for api`() = runTest {
        // given
        val timeline = Timeline.Public
        val manager = StatusListManager(timeline, api)
        val pageSize = 20
        val maxId = "maxId"
        // when
        val result = manager.loadStatusList(pageSize, maxId)
        // then
        assertThat(result.isSuccess).isTrue()
        verifySuspend(exactly(1)) { api.publicTimeline(any(), any()) }
    }

    @Test
    fun `loadStatusList for hashtag timeline calls for api`() = runTest {
        // given
        val tag = "tag"
        val timeline = Timeline.HashTag(tag)
        val manager = StatusListManager(timeline, api)
        val pageSize = 20
        val maxId = "maxId"
        // when
        val result = manager.loadStatusList(pageSize, maxId)
        // then
        assertThat(result.isSuccess).isTrue()
        verifySuspend(exactly(1)) { api.hashTagTimeline(tag, any(), any()) }
    }

    @Test
    fun `loadStatusList for list timeline calls for api`() = runTest {
        // given
        val listId = "listId"
        val timeline = Timeline.List(listId)
        val manager = StatusListManager(timeline, api)
        val pageSize = 20
        val maxId = "maxId"
        // when
        val result = manager.loadStatusList(pageSize, maxId)
        // then
        assertThat(result.isSuccess).isTrue()
        verifySuspend(exactly(1)) { api.listTimeline(listId, any()) }
    }
}
