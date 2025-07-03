package com.sedsoftware.tackle.status.integration

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.status.Responses
import com.sedsoftware.tackle.status.StatusComponent
import com.sedsoftware.tackle.status.StatusComponentGateways
import com.sedsoftware.tackle.status.model.StatusAction
import com.sedsoftware.tackle.status.model.StatusContextAction
import com.sedsoftware.tackle.utils.test.ComponentTest
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class StatusComponentTest : ComponentTest<StatusComponent>() {

    private val testStatus: Status = Responses.status

    private val api: StatusComponentGateways.Api = mock {
        everySuspend { delete(any(), any()) } returns testStatus
        everySuspend { translate(any(), any()) } returns Responses.translation
        everySuspend { vote(any(), any()) } returns Responses.poll
        everySuspend { favourite(any()) } returns testStatus.copy(favourited = true)
        everySuspend { unfavourite(any()) } returns testStatus.copy(favourited = false)
        everySuspend { boost(any()) } returns testStatus.copy(reblogged = true)
        everySuspend { unboost(any()) } returns testStatus.copy(reblogged = false)
        everySuspend { bookmark(any()) } returns testStatus.copy(bookmarked = true)
        everySuspend { unbookmark(any()) } returns testStatus.copy(bookmarked = false)
        everySuspend { pin(any()) } returns testStatus.copy(pinned = true)
        everySuspend { unpin(any()) } returns testStatus.copy(pinned = false)
        everySuspend { mute(any()) } returns testStatus.copy(muted = true)
        everySuspend { unmute(any()) } returns testStatus.copy(muted = false)
    }

    private val tools: StatusComponentGateways.Tools = mock {
        every { getCurrentLocale() } returns AppLocale("English", "en")
        every { openUrl(any()) } returns Unit
        every { shareUrl(any(), any()) } returns Unit
    }

    private val activeModel: StatusComponent.Model
        get() = component.model.value

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `onMenuActionClick should run menu actions`() = runTest {
        // given
        // when
        component.onMenuActionClick(StatusContextAction.TRANSLATE)
        // then
        assertThat(activeModel.translation).isNotNull()
        assertThat(activeModel.translationDisplayed).isTrue()
        // when
        component.onMenuActionClick(StatusContextAction.SHOW_ORIGINAL)
        // then
        assertThat(activeModel.translationDisplayed).isFalse()
        // when
        component.onMenuActionClick(StatusContextAction.DELETE)
        // then
        verifySuspend(exactly(1)) { api.delete(testStatus.id, true) }
        val output = componentOutput.firstOrNull { it is ComponentOutput.SingleStatus.Deleted } as ComponentOutput.SingleStatus.Deleted
        assertThat(output.statusId).isEqualTo(testStatus.id)
        // when
        component.onMenuActionClick(StatusContextAction.PIN)
        // then
        assertThat(activeModel.status.pinned).isTrue()
        verifySuspend(exactly(1)) { api.pin(testStatus.id) }
        // when
        component.onMenuActionClick(StatusContextAction.UNPIN)
        // then
        assertThat(activeModel.status.pinned).isFalse()
        verifySuspend(exactly(1)) { api.unpin(testStatus.id) }
        // when
        component.onMenuActionClick(StatusContextAction.BOOKMARK)
        // then
        assertThat(activeModel.status.bookmarked).isTrue()
        verifySuspend(exactly(1)) { api.bookmark(testStatus.id) }
        // when
        component.onMenuActionClick(StatusContextAction.UNBOOKMARK)
        // then
        assertThat(activeModel.status.bookmarked).isFalse()
        verifySuspend(exactly(1)) { api.unbookmark(testStatus.id) }
        // when
        component.onMenuActionClick(StatusContextAction.MUTE)
        // then
        assertThat(activeModel.status.muted).isTrue()
        verifySuspend(exactly(1)) { api.mute(testStatus.id) }
        // when
        component.onMenuActionClick(StatusContextAction.UNMUTE)
        // then
        assertThat(activeModel.status.muted).isFalse()
        verifySuspend(exactly(1)) { api.unmute(testStatus.id) }
    }

    @Test
    fun `on favourite click should favourite status`() = runTest {
        // given
        // when
        component.onStatusAction(StatusAction.FAVOURITE)
        // then
        assertThat(activeModel.status.favourited).isTrue()
        verifySuspend(exactly(1)) { api.favourite(testStatus.id) }
        // when
        component.onStatusAction(StatusAction.FAVOURITE)
        // then
        assertThat(activeModel.status.favourited).isFalse()
        verifySuspend(exactly(1)) { api.unfavourite(testStatus.id) }
    }

    @Test
    fun `on reblog click should reblog status`() = runTest {
        // given
        // when
        component.onStatusAction(StatusAction.REBLOG)
        // then
        assertThat(activeModel.status.reblogged).isTrue()
        verifySuspend(exactly(1)) { api.boost(testStatus.id) }
        // when
        component.onStatusAction(StatusAction.REBLOG)
        // then
        assertThat(activeModel.status.reblogged).isFalse()
        verifySuspend(exactly(1)) { api.unboost(testStatus.id) }
    }

    @Test
    fun `onMenuRequest should control menu visibility`() = runTest {
        // given
        // when
        component.onMenuRequest(true)
        // then
        assertThat(activeModel.menuVisible).isTrue()
        // when
        component.onMenuRequest(false)
        // then
        assertThat(activeModel.menuVisible).isFalse()
    }

    @Test
    fun `onPollSelect should update poll state`() = runTest {
        // given
        val selectedOption1 = 2
        val selectedOption2 = 0
        // when
        component.onPollSelect(selectedOption2, true)
        component.onPollSelect(selectedOption1, true)
        // then
        assertThat(activeModel.status.poll?.ownVotes).isEqualTo(listOf(0, 2))
    }

    @Test
    fun `onVoteClick should call for vote`() = runTest {
        // given
        component.onPollSelect(1, false)
        // when
        component.onVoteClick()

        // then
        assertThat(activeModel.status.poll).isEqualTo(Responses.poll)
        verifySuspend(exactly(1)) { api.vote(activeModel.status.poll?.id.orEmpty(), listOf(1)) }
    }

    @Test
    fun `on share click should call for share`() = runTest {
        // given
        // when
        component.onStatusAction(StatusAction.SHARE)
        // then
        verify(exactly(1)) { tools.shareUrl(testStatus.account.displayName, testStatus.url) }
    }


    @Test
    fun `onUrlClick should call for open url`() = runTest {
        // given
        val url = "https://google.com"
        // when
        component.onUrlClick(url)
        // then
        verify(exactly(1)) { tools.openUrl(url) }
    }

    @Test
    fun `on reply click should publish output for replying`() = runTest {
        // given
        // when
        component.onStatusAction(StatusAction.REPLY)
        // then
        val output = componentOutput.firstOrNull { it is ComponentOutput.SingleStatus.ReplyCalled } as ComponentOutput.SingleStatus.ReplyCalled
        assertThat(output.statusId).isEqualTo(testStatus.id)
    }

    @Test
    fun `onHashTagClick should publish output with hash tag`() = runTest {
        // given
        val hashtag = "tag"
        // when
        component.onHashTagClick(hashtag)
        // then
        val output = componentOutput.firstOrNull { it is ComponentOutput.SingleStatus.HashTagClicked } as ComponentOutput.SingleStatus.HashTagClicked
        assertThat(output.hashTag).isEqualTo(hashtag)
    }

    @Test
    fun `onMentionClick should publish output with mention`() = runTest {
        // given
        val mention = "@mention"
        // when
        component.onMentionClick(mention)
        // then
        val output = componentOutput.firstOrNull { it is ComponentOutput.SingleStatus.MentionClicked } as ComponentOutput.SingleStatus.MentionClicked
        assertThat(output.mention).isEqualTo(mention)
    }

    override fun createComponent(): StatusComponent =
        StatusComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            api = api,
            tools = tools,
            dispatchers = testDispatchers,
            output = { componentOutput.add(it) },
            status = testStatus,
            extendedInfo = false,
            isOwn = true,
        )
}
