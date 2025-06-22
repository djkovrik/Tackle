package com.sedsoftware.tackle.editor.store

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.model.NewStatusBundle
import com.sedsoftware.tackle.editor.EditorComponentGateways
import com.sedsoftware.tackle.editor.Instances
import com.sedsoftware.tackle.editor.Responses
import com.sedsoftware.tackle.editor.domain.EditorManager
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.model.EditorInputHintRequest
import com.sedsoftware.tackle.editor.stubs.EditorComponentToolsStub
import com.sedsoftware.tackle.utils.test.StoreTest
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class EditorStoreTest : StoreTest<EditorStore.Intent, EditorStore.State, EditorStore.Label>() {

    private var nowProviderStub: Instant = System.now()

    private val api: EditorComponentGateways.Api = mock {
        everySuspend { getServerEmojis() } returns Responses.emojis
        everySuspend { sendFile(any(), any()) } returns Responses.buildAttachmentResponse(null, null)
        everySuspend { search(any()) } returns Responses.searchResponseDefault
        everySuspend { sendStatus(any()) } returns Responses.statusNormal
        everySuspend { sendStatusScheduled(any()) } returns Responses.statusScheduled
        everySuspend { updateFile(any(), any(), any()) } returns Responses.buildAttachmentResponse(null, null)
        everySuspend { getFile(any()) } returns Responses.buildAttachmentResponse(null, null)
    }

    private val database: EditorComponentGateways.Database = mock {
        everySuspend { cacheServerEmojis(any()) } returns Unit
        everySuspend { observeCachedEmojis() } returns flowOf(Instances.customEmojiList.groupBy { it.category })
        everySuspend { findEmojis(any()) } returns flowOf(Instances.customEmojiList)
        everySuspend { getCachedInstanceInfo() } returns flowOf(Instances.instanceInfo)
    }

    private val tools: EditorComponentGateways.Tools = EditorComponentToolsStub()

    private val manager: EditorManager = EditorManager(
        api = api,
        database = database,
        tools = tools,
        nowInstantProvider = { nowProviderStub },
        timeZoneProvider = { TimeZone.UTC },
    )

    private val todayMock: LocalDateTime by lazy {
        LocalDateTime.parse("2024-08-12T12:34:56.120")
    }

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `FetchCachedInstanceInfo should call for FetchCachedInstanceInfo`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        // then
        assertThat(store.state.instanceInfo).isEqualTo(Instances.instanceInfo)
    }

    @Test
    fun `error on store init should show error message`() = runTest {
        // given
        everySuspend { database.getCachedInstanceInfo() } throws IllegalStateException("Test")
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        // then
        assertThat(labels.count { it is EditorStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `store init should initialize current hour and minute`() = runTest {
        // given
        val minutesGap = 15
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        // then
        assertThat(store.state.scheduledHour).isEqualTo(12)
        assertThat(store.state.scheduledMinute).isEqualTo(34 + minutesGap)
    }

    @Test
    fun `OnTextInput should update status text and selection`() = runTest {
        // given
        val text = "Some text"
        val selection = text.length to text.length
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnTextInput(text, selection))
        // then
        assertThat(store.state.statusText).isEqualTo(text)
        assertThat(store.state.statusTextSelection).isEqualTo(selection)
        assertThat(store.state.statusCharactersLeft).isEqualTo(store.state.statusCharactersLimit - text.length)
    }

    @Test
    fun `OnTextInput should not enter text which exceeds the limit`() = runTest {
        // given
        val sb = StringBuilder()
        val limit = Instances.instanceConfig.statuses.maxCharacters
        repeat(limit + 10) { sb.append("X") }
        val text = sb.toString()
        val selection = text.length to text.length
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnTextInput(text, selection))
        // then
        assertThat(store.state.statusTextSelection).isEqualTo(limit to limit)
        assertThat(store.state.statusCharactersLeft).isEqualTo(0)
        assertThat(store.state.suggestions).isEmpty()
    }

    @Test
    fun `OnEmojiSelected should update status text and selection`() = runTest {
        // given
        val emoji = Instances.emoji
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnEmojiSelected(emoji))
        // then
        assertThat(store.state.statusText).isEqualTo(":${emoji.shortcode}:")
    }

    @Test
    fun `store creation should load cached config`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        // then
        assertThat(store.state.instanceInfoLoaded).isTrue()
        assertThat(store.state.instanceInfo.domain).isNotEmpty()
        assertThat(store.state.instanceInfo.config.statuses.maxMediaAttachments).isNotEqualTo(0)
        assertThat(store.state.statusCharactersLeft).isGreaterThan(0)
        assertThat(labels.isNotEmpty())
        val label = labels.first()
        assertThat(label).hasClass(EditorStore.Label.InstanceConfigLoaded::class)
        label as EditorStore.Label.InstanceConfigLoaded
        assertThat(label.config).isEqualTo(Instances.instanceConfig)
    }

    @Test
    fun `OnTextInput should call for account hint suggestions`() = runTest {
        // given
        val text1 = "Some text"
        val text2 = "Some text @"
        val text3 = "Some text @a"
        val text4 = "Some text @ab"
        val text5 = "Clear"

        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnTextInput(text1, text1.length to text1.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.accept(EditorStore.Intent.OnTextInput(text2, text2.length to text2.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.accept(EditorStore.Intent.OnTextInput(text3, text3.length to text3.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.accept(EditorStore.Intent.OnTextInput(text4, text4.length to text4.length))
        // then
        assertThat(store.state.suggestions).isNotEmpty()
        assertThat(store.state.currentSuggestionRequest).isEqualTo(EditorInputHintRequest.Accounts("@ab"))

        // when
        store.accept(EditorStore.Intent.OnTextInput(text5, text5.length to text5.length))
        // then
        assertThat(store.state.suggestions).isEmpty()
    }

    @Test
    fun `account suggestion loading error should show error message`() = runTest {
        // given
        val text = "Some text @ab"
        everySuspend { api.search(any()) } throws IllegalStateException("Test")
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnTextInput(text, text.length to text.length))
        // then
        assertThat(labels.count { it is EditorStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `OnTextInput should call for emoji hint suggestions`() = runTest {
        // given
        val text1 = "Some text"
        val text2 = "Some text :"
        val text3 = "Some text :a"
        val text4 = "Some text :ab"
        val text5 = "Clear"
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnTextInput(text1, text1.length to text1.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.accept(EditorStore.Intent.OnTextInput(text2, text2.length to text2.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.accept(EditorStore.Intent.OnTextInput(text3, text3.length to text3.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.accept(EditorStore.Intent.OnTextInput(text4, text4.length to text4.length))
        // then
        assertThat(store.state.suggestions).isNotEmpty()
        assertThat(store.state.currentSuggestionRequest).isEqualTo(EditorInputHintRequest.Emojis(":ab"))

        // when
        store.accept(EditorStore.Intent.OnTextInput(text5, text5.length to text5.length))
        // then
        assertThat(store.state.suggestions).isEmpty()
    }

    @Test
    fun `emoji suggestion loading error should show error message`() = runTest {
        // given
        val text = "Some text :ab"
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        // then
        assertThat(store.state.instanceInfo).isEqualTo(Instances.instanceInfo)
        // when
        everySuspend { database.findEmojis(any()) } throws IllegalStateException("Test")
        store.accept(EditorStore.Intent.OnTextInput(text, text.length to text.length))
        // then
        assertThat(labels.count { it is EditorStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `OnTextInput should call for hashtag hint suggestions`() = runTest {
        // given
        val text1 = "Some text"
        val text2 = "Some text #"
        val text3 = "Some text #a"
        val text4 = "Some text #ab"
        val text5 = "Clear"

        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnTextInput(text1, text1.length to text1.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.accept(EditorStore.Intent.OnTextInput(text2, text2.length to text2.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.accept(EditorStore.Intent.OnTextInput(text3, text3.length to text3.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.accept(EditorStore.Intent.OnTextInput(text4, text4.length to text4.length))
        // then
        assertThat(store.state.suggestions).isNotEmpty()
        assertThat(store.state.currentSuggestionRequest).isEqualTo(EditorInputHintRequest.HashTags("#ab"))

        // when
        store.accept(EditorStore.Intent.OnTextInput(text5, text5.length to text5.length))
        // then
        assertThat(store.state.suggestions).isEmpty()
    }

    @Test
    fun `hashTag suggestion loading error should show error message`() = runTest {
        // given
        val text = "Some text #ab"
        everySuspend { api.search(any()) } throws IllegalStateException("Test")
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnTextInput(text, text.length to text.length))
        // then
        assertThat(labels.count { it is EditorStore.Label.ErrorCaught }).isEqualTo(1)
    }

    @Test
    fun `OnInputHintSelected should insert input hint`() = runTest {
        // given
        val hint = EditorInputHintItem.Account("", "testtest", "")
        val text = "Some text @tes"
        val expectedText = "Some text @testtest"
        val selection = text.length to text.length
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnTextInput(text, selection))
        // then
        assertThat(store.state.currentSuggestionRequest).isEqualTo(EditorInputHintRequest.Accounts("@tes"))
        // and when
        store.accept(EditorStore.Intent.OnInputHintSelected(hint))
        // then
        assertThat(store.state.statusText).isEqualTo(expectedText)
        assertThat(store.state.statusTextSelection).isEqualTo(expectedText.length to expectedText.length)
    }

    @Test
    fun `OnDatePickerRequested should update dialog visibility`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnDatePickerRequested(true))
        // then
        assertThat(store.state.datePickerVisible).isTrue()
        // and when
        store.accept(EditorStore.Intent.OnDatePickerRequested(false))
        // then
        assertThat(store.state.datePickerVisible).isFalse()
    }

    @Test
    fun `OnDateScheduled should update scheduled date`() = runTest {
        // given
        val newDate = 1234567L
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnDateScheduled(newDate))
        // then
        assertThat(store.state.scheduledDate).isEqualTo(newDate)
    }

    @Test
    fun `OnTimePickerRequested should update dialog visibility`() = runTest {
        // given
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnTimePickerRequested(true))
        // then
        assertThat(store.state.timePickerVisible).isTrue()
        // and when
        store.accept(EditorStore.Intent.OnTimePickerRequested(false))
        // then
        assertThat(store.state.timePickerVisible).isFalse()
    }

    @Test
    fun `OnTimeScheduled should update scheduled time`() = runTest {
        // given
        val hour = 16
        val minute = 54
        val format = false
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnTimeScheduled(hour, minute, format))
        // then
        assertThat(store.state.scheduledHour).isEqualTo(hour)
        assertThat(store.state.scheduledMinute).isEqualTo(minute)
        assertThat(store.state.scheduledIn24hFormat).isEqualTo(format)
    }

    @Test
    fun `OnDateScheduled should reset scheduled date and time`() = runTest {
        // given
        val hour = 16
        val minute = 54
        val newDate = 1234567L
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnTimeScheduled(hour, minute, true))
        store.accept(EditorStore.Intent.OnDateScheduled(newDate))
        // then
        assertThat(store.state.scheduledHour).isEqualTo(hour)
        assertThat(store.state.scheduledMinute).isEqualTo(minute)
        assertThat(store.state.scheduledDate).isEqualTo(newDate)
        // and when
        store.accept(EditorStore.Intent.OnScheduledDateTimeReset)
        assertThat(store.state.scheduledHour).isEqualTo(-1)
        assertThat(store.state.scheduledMinute).isEqualTo(-1)
        assertThat(store.state.scheduledDate).isEqualTo(-1L)
    }

    @Test
    fun `SendStatus should publish label on success`() = runTest {
        // given
        val bundle = NewStatusBundle.Builder().status("Test").build()
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.SendStatus(bundle))
        // then
        assertThat(labels.count { it is EditorStore.Label.StatusSent }).isEqualTo(1)
        assertThat(store.state.sendingActive).isTrue()
    }

    @Test
    fun `SendStatus for scheduled status should publish label on success`() = runTest {
        // given
        val now: LocalDateTime = LocalDateTime.parse("2024-08-12T12:34:56.120")
        nowProviderStub = now.toInstant(TimeZone.UTC)

        val hour = 16
        val minute = 54
        val newDate = 1819497600000L
        val bundle = NewStatusBundle.Builder()
            .status("Test")
            .scheduledAtDate(1819497600000L)
            .scheduledAtHour(hour)
            .scheduledAtMinute(minute)
            .build()
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        store.accept(EditorStore.Intent.OnTimeScheduled(hour, minute, true))
        store.accept(EditorStore.Intent.OnDateScheduled(newDate))
        store.accept(EditorStore.Intent.SendStatus(bundle))
        // then
        assertThat(labels.count { it is EditorStore.Label.ScheduledStatusSent }).isEqualTo(1)
        assertThat(store.state.sendingActive).isTrue()
    }

    @Test
    fun `SendStatus should throw an error on failure`() = runTest {
        // given
        val bundle = NewStatusBundle.Builder().status("Test").build()
        // when
        store.init()
        store.accept(EditorStore.Intent.FetchCachedInstanceInfo)
        everySuspend { api.sendStatus(any()) } throws IllegalStateException("Test")
        store.accept(EditorStore.Intent.SendStatus(bundle))
        // then
        assertThat(labels.count { it is EditorStore.Label.ErrorCaught }).isEqualTo(1)
        assertThat(store.state.sendingActive).isFalse()
    }

    override fun createStore(): Store<EditorStore.Intent, EditorStore.State, EditorStore.Label> =
        EditorStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = manager,
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
            today = { todayMock },
        ).create(autoInit = false)
}
