package com.sedsoftware.tackle.home.integration

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.model.Account
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.domain.model.type.ShortDateUnit
import com.sedsoftware.tackle.domain.model.type.StatusVisibility.PUBLIC
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.status.StatusComponentGateways
import com.sedsoftware.tackle.utils.extension.toLocalDateCustom
import com.sedsoftware.tackle.utils.extension.toLocalDateTimeCustom
import com.sedsoftware.tackle.utils.test.ComponentTest
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
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

    @Test
    @OptIn(ExperimentalDecomposeApi::class)
    fun `showCreatedStatus displays created status`() = runTest {
        // given
        every { settings.ownUserId } returns "userId"
        val newStatus = Status(
            id = "id",
            uri = "uri",
            createdAt = LocalDateTime.parse("2023-01-02T23:40:57.120"),
            createdAtShort = ShortDateUnit.Now,
            createdAtPretty = "02.01.2023 23:40",
            editedAt = LocalDateTime.parse("2023-01-02T23:40:57.120"),
            editedAtShort = ShortDateUnit.Now,
            editedAtPretty = "02.01.2023 23:40",
            account = Account(
                id = "12345",
                username = "djkovrik",
                acct = "djkovrik",
                displayName = "djkovrik",
                note = "123",
                url = "url",
                uri = "uri",
                avatar = "url",
                avatarStatic = "url",
                header = "url",
                headerStatic = "url",
                locked = false,
                fields = emptyList(),
                emojis = emptyList(),
                bot = false,
                group = false,
                discoverable = false,
                noIndex = false,
                suspended = false,
                limited = false,
                createdAt = "2023-01-02T23:40:57.12".toLocalDateTimeCustom(),
                lastStatusAt = "2023-01-02".toLocalDateCustom(),
                statusesCount = 123L,
                followersCount = 321L,
                followingCount = 1234L,
                moved = null,
                source = null,
                role = null,
            ),
            content = "Test content",
            contentAsPlainText = "Test content",
            visibility = PUBLIC,
            sensitive = false,
            spoilerText = "",
            mediaAttachments = emptyList(),
            application = null,
            mentions = emptyList(),
            tags = emptyList(),
            emojis = emptyList(),
            reblogsCount = 1,
            favouritesCount = 2,
            repliesCount = 3,
            url = "url",
            inReplyToId = "",
            inReplyToAccountId = "",
            reblog = null,
            poll = null,
            language = "en",
            text = "Text",
            favourited = false,
            reblogged = false,
            muted = false,
            bookmarked = false,
            pinned = false,
            filtered = emptyList(),
            card = null,
        )

        // when
        component.showCreatedStatus(newStatus)
        // then
        assertThat(component.homeTimeline.items.value.items.first()).isEqualTo(newStatus)
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
