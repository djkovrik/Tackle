package com.sedsoftware.tackle.home.integration

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.status.StatusComponentGateways
import com.sedsoftware.tackle.utils.test.ComponentTest
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class HomeTabComponentTest : ComponentTest<HomeTabComponent>() {

    private val api: StatusComponentGateways.Api = mock<StatusComponentGateways.Api>()
    private val settings: StatusComponentGateways.Settings = mock<StatusComponentGateways.Settings>()
    private val tools: StatusComponentGateways.Tools = mock<StatusComponentGateways.Tools>()

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `onNewPostClick should publish output`() = runTest {
        // given
        // when
        component.onNewPostClick()
        // then
        assertThat(componentOutput.count { it is ComponentOutput.HomeTab.EditorRequested }).isEqualTo(1)
    }

    @Test
    fun `onScheduledPostsClick should publish output`() = runTest {
        // given
        // when
        component.onScheduledPostsClick()
        // then
        assertThat(componentOutput.count { it is ComponentOutput.HomeTab.ScheduledStatusesRequested }).isEqualTo(1)
    }

    override fun createComponent(): HomeTabComponent =
        HomeTabComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            api = api,
            settings = settings,
            tools = tools,
            dispatchers = testDispatchers,
            output = { componentOutput.add(it) },
        )
}
