@file:OptIn(ExperimentalDecomposeApi::class)

package com.sedsoftware.tackle.statuslist.integration

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotEqualTo
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.items.ChildItems
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.Timeline
import com.sedsoftware.tackle.status.StatusComponent
import com.sedsoftware.tackle.status.StatusComponentGateways
import com.sedsoftware.tackle.status.model.StatusContextAction
import com.sedsoftware.tackle.statuslist.Responses
import com.sedsoftware.tackle.statuslist.StatusListComponent
import com.sedsoftware.tackle.statuslist.store.StatusListStoreProvider
import com.sedsoftware.tackle.utils.test.ComponentTest
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class StatusListComponentTest : ComponentTest<StatusListComponent>() {

    private val testUserId: String = "testUserId"
    private val timeline: Timeline = Timeline.Home
    private val itemIndexToDelete: Int = 1

    private val api: StatusComponentGateways.Api = mock {
        everySuspend { homeTimeline(any()) } returns
                List(StatusListStoreProvider.DEFAULT_PAGE_SIZE) { Responses.status.copy(id = "$it") }
        everySuspend { delete(any(), any()) } returns
                List(StatusListStoreProvider.DEFAULT_PAGE_SIZE) { Responses.status.copy(id = "$it") }[itemIndexToDelete]
    }

    private val settings: StatusComponentGateways.Settings = mock {
        every { ownUserId } returns testUserId
    }

    private val tools: StatusComponentGateways.Tools = mock {
        every { getCurrentLocale() } returns AppLocale("English", "en")
        every { openUrl(any()) } returns Unit
        every { shareUrl(any(), any()) } returns Unit
    }

    private val activeModel: StatusListComponent.Model
        get() = component.model.value

    private val activeComponents: ChildItems<Status, StatusComponent>
        get() = component.items.value

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `component creation loads timeline`() = runTest {
        // given
        // when
        // then
        assertThat(activeModel.initialProgressVisible).isFalse()
        assertThat(activeModel.loadMoreProgressVisible).isFalse()
        assertThat(activeModel.emptyPlaceholderVisible).isFalse()
        assertThat(activeComponents.items.size).isEqualTo(StatusListStoreProvider.DEFAULT_PAGE_SIZE)
        verifySuspend(exactly(1)) { api.homeTimeline(any()) }
    }

    @Test
    fun `onPullToRefresh should call for components refresh`() = runTest {
        // given
        verifySuspend(exactly(1)) { api.homeTimeline(any()) }
        // when
        component.onPullToRefresh()
        // then
        verifySuspend(exactly(1)) { api.homeTimeline(any()) }
    }

    @Test
    fun `onLoadMoreRequest should call for loading more components`() = runTest {
        // given
        verifySuspend(exactly(1)) { api.homeTimeline(any()) }
        resetAnswers(api)
        everySuspend { api.homeTimeline(any()) } returns List(StatusListStoreProvider.DEFAULT_PAGE_SIZE) { Responses.status.copy(id = "$it $it") }
        // when
        component.onLoadMoreRequest()
        // then
        verifySuspend(exactly(1)) { api.homeTimeline(any()) }
        assertThat(activeComponents.items.size).isEqualTo(StatusListStoreProvider.DEFAULT_PAGE_SIZE * 2)
    }

    @Test
    fun `status deletion should refresh active model`() = runTest {
        // given
        assertThat(activeComponents.items.size).isEqualTo(StatusListStoreProvider.DEFAULT_PAGE_SIZE)
        val statusToDelete = activeComponents.items.first()
        val componentToDelete = activeComponents.activeItems[statusToDelete]?.first
        // when
        componentToDelete?.onMenuActionClick(StatusContextAction.DELETE)
        // then
        assertThat(activeComponents.items).doesNotContain(componentToDelete)
    }

    @Test
    fun `showCreatedStatus should prepend status to items list`() = runTest {
        // given
        val newStatus = Responses.status.copy(id = "new status")
        // when
        component.showCreatedStatus(newStatus)
        // then
        assertThat(activeComponents.items.first()).isEqualTo(newStatus)
    }

    @Test
    fun `createChild should create child component`() = runTest {
        // given
        val newStatus = Responses.status.copy(id = "New status")
        val comp = component as StatusListComponentDefault
        // when
        val result = comp.createChild(newStatus, DefaultComponentContext(lifecycle))
        // then
        assertThat(result.model.value.status).isEqualTo(newStatus)
    }


    @Test
    fun `onStatusComponentOutput should delete status`() = runTest {
        // given
        val comp = component as StatusListComponentDefault
        val statusIdToDelete = activeComponents.items[0].id
        // when
        comp.onStatusComponentOutput(ComponentOutput.SingleStatus.Deleted(statusIdToDelete))
        // then
        assertThat(activeComponents.items[0].id).isNotEqualTo(statusIdToDelete)
    }

    @Test
    fun `onStatusComponentOutput should call for outer output`() = runTest {
        // given
        val comp = component as StatusListComponentDefault
        val statusOutput = ComponentOutput.SingleStatus.MentionClicked("")
        // when
        comp.onStatusComponentOutput(statusOutput)
        // then
        assertThat(componentOutput).contains(statusOutput)
    }


    override fun createComponent(): StatusListComponent =
        StatusListComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            api = api,
            settings = settings,
            tools = tools,
            dispatchers = testDispatchers,
            output = { componentOutput.add(it) },
            timeline = timeline,
        )
}
