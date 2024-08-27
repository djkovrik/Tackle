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
import com.sedsoftware.tackle.editor.EditorTabComponentGateways
import com.sedsoftware.tackle.editor.domain.EditorTabManager
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.model.EditorInputHintRequest
import com.sedsoftware.tackle.editor.store.EditorTabStore.Intent
import com.sedsoftware.tackle.editor.store.EditorTabStore.Label
import com.sedsoftware.tackle.editor.store.EditorTabStore.State
import com.sedsoftware.tackle.editor.stubs.EditorTabComponentApiStub
import com.sedsoftware.tackle.editor.stubs.EditorTabComponentDatabaseStub
import com.sedsoftware.tackle.editor.stubs.EditorTabComponentToolsStub
import com.sedsoftware.tackle.editor.stubs.EmojiStub
import com.sedsoftware.tackle.editor.stubs.InstanceStub
import com.sedsoftware.tackle.utils.test.StoreTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class EditorTabStoreTest : StoreTest<Intent, State, Label>() {

    private var nowProviderStub: Instant = System.now()

    private val api: EditorTabComponentApiStub = EditorTabComponentApiStub()
    private val database: EditorTabComponentGateways.Database = EditorTabComponentDatabaseStub()
    private val tools: EditorTabComponentGateways.Tools = EditorTabComponentToolsStub()

    private val manager: EditorTabManager = EditorTabManager(
        api = api,
        database = database,
        tools = tools,
        nowInstantProvider = { nowProviderStub },
        timeZoneProvider = { TimeZone.UTC },
    )


    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    private val todayMock: LocalDateTime by lazy {
        LocalDateTime.parse("2024-08-12T12:34:56.120")
    }

    @Test
    fun `store init should initialize current hour and minute`() = runTest {
        // given
        val minutesGap = 10
        // when
        store.init()
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
        store.accept(Intent.OnTextInput(text, selection))
        // then
        assertThat(store.state.statusText).isEqualTo(text)
        assertThat(store.state.statusTextSelection).isEqualTo(selection)
        assertThat(store.state.statusCharactersLeft).isEqualTo(store.state.statusCharactersLimit - text.length)
    }

    @Test
    fun `OnEmojiSelect should update status text and selection`() = runTest {
        // given
        val emoji = EmojiStub.single
        // when
        store.init()
        store.accept(Intent.OnEmojiSelect(emoji))
        // then
        assertThat(store.state.statusText).isEqualTo(":${emoji.shortcode}:")
    }

    @Test
    fun `store creation should load cached config`() = runTest {
        // given
        // when
        store.init()
        // then
        assertThat(store.state.instanceInfoLoaded).isTrue()
        assertThat(store.state.instanceInfo.domain).isNotEmpty()
        assertThat(store.state.instanceInfo.config.statuses.maxMediaAttachments).isNotEqualTo(0)
        assertThat(store.state.statusCharactersLeft).isGreaterThan(0)
        assertThat(labels.isNotEmpty())
        val label = labels.first()
        assertThat(label).hasClass(Label.InstanceConfigLoaded::class)
        label as Label.InstanceConfigLoaded
        assertThat(label.config).isEqualTo(InstanceStub.config)
    }

    @Test
    fun `OnTextInput should call for account hint suggestions`() = runTest {
        // given
        val text1 = "Some text"
        val text2 = "Some text @"
        val text3 = "Some text @a"
        val text4 = "Some text @ab"

        // when
        store.init()
        store.accept(Intent.OnTextInput(text1, text1.length to text1.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.init()
        store.accept(Intent.OnTextInput(text2, text2.length to text2.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.init()
        store.accept(Intent.OnTextInput(text3, text3.length to text3.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.init()
        store.accept(Intent.OnTextInput(text4, text4.length to text4.length))
        // then
        assertThat(store.state.suggestions).isNotEmpty()
        assertThat(store.state.currentSuggestionRequest).isEqualTo(EditorInputHintRequest.Accounts("@ab"))
    }

    @Test
    fun `OnTextInput should call for emoji hint suggestions`() = runTest {
        // given
        val text1 = "Some text"
        val text2 = "Some text :"
        val text3 = "Some text :a"
        val text4 = "Some text :ab"

        // when
        store.init()
        store.accept(Intent.OnTextInput(text1, text1.length to text1.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.init()
        store.accept(Intent.OnTextInput(text2, text2.length to text2.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.init()
        store.accept(Intent.OnTextInput(text3, text3.length to text3.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.init()
        store.accept(Intent.OnTextInput(text4, text4.length to text4.length))
        // then
        assertThat(store.state.suggestions).isNotEmpty()
        assertThat(store.state.currentSuggestionRequest).isEqualTo(EditorInputHintRequest.Emojis(":ab"))
    }

    @Test
    fun `OnTextInput should call for hashtag hint suggestions`() = runTest {
        // given
        val text1 = "Some text"
        val text2 = "Some text #"
        val text3 = "Some text #a"
        val text4 = "Some text #ab"

        // when
        store.init()
        store.accept(Intent.OnTextInput(text1, text1.length to text1.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.init()
        store.accept(Intent.OnTextInput(text2, text2.length to text2.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.init()
        store.accept(Intent.OnTextInput(text3, text3.length to text3.length))
        // then
        assertThat(store.state.suggestions).isEmpty()

        // when
        store.init()
        store.accept(Intent.OnTextInput(text4, text4.length to text4.length))
        // then
        assertThat(store.state.suggestions).isNotEmpty()
        assertThat(store.state.currentSuggestionRequest).isEqualTo(EditorInputHintRequest.HashTags("#ab"))
    }

    @Test
    fun `OnInputHintSelect should insert input hint`() = runTest {
        // given
        val hint = EditorInputHintItem.Account("", "testtest", "")
        val text = "Some text @tes"
        val expectedText = "Some text @testtest"
        val selection = text.length to text.length
        // when
        store.init()
        store.accept(Intent.OnTextInput(text, selection))
        // then
        assertThat(store.state.currentSuggestionRequest).isEqualTo(EditorInputHintRequest.Accounts("@tes"))
        // and when
        store.accept(Intent.OnInputHintSelect(hint))
        // then
        assertThat(store.state.statusText).isEqualTo(expectedText)
        assertThat(store.state.statusTextSelection).isEqualTo(expectedText.length to expectedText.length)
    }

    @Test
    fun `OnRequestDatePicker should update dialog visibility`() = runTest {
        // given
        // when
        store.init()
        store.accept(Intent.OnRequestDatePicker(true))
        // then
        assertThat(store.state.datePickerVisible).isTrue()
        // and when
        store.accept(Intent.OnRequestDatePicker(false))
        // then
        assertThat(store.state.datePickerVisible).isFalse()
    }

    @Test
    fun `OnScheduleDate should update scheduled date`() = runTest {
        // given
        val newDate = 1234567L
        // when
        store.init()
        store.accept(Intent.OnScheduleDate(newDate))
        // then
        assertThat(store.state.scheduledDate).isEqualTo(newDate)
    }

    @Test
    fun `OnRequestTimePicker should update dialog visibility`() = runTest {
        // given
        // when
        store.init()
        store.accept(Intent.OnRequestTimePicker(true))
        // then
        assertThat(store.state.timePickerVisible).isTrue()
        // and when
        store.accept(Intent.OnRequestTimePicker(false))
        // then
        assertThat(store.state.timePickerVisible).isFalse()
    }

    @Test
    fun `OnScheduleTime should update scheduled time`() = runTest {
        // given
        val hour = 16
        val minute = 54
        val format = false
        // when
        store.init()
        store.accept(Intent.OnScheduleTime(hour, minute, format))
        // then
        assertThat(store.state.scheduledHour).isEqualTo(hour)
        assertThat(store.state.scheduledMinute).isEqualTo(minute)
        assertThat(store.state.scheduledIn24hFormat).isEqualTo(format)
    }

    @Test
    fun `OnScheduledDateTimeReset should reset scheduled date and time`() = runTest {
        // given
        val hour = 16
        val minute = 54
        val newDate = 1234567L
        // when
        store.init()
        store.accept(Intent.OnScheduleTime(hour, minute, true))
        store.accept(Intent.OnScheduleDate(newDate))
        // then
        assertThat(store.state.scheduledHour).isEqualTo(hour)
        assertThat(store.state.scheduledMinute).isEqualTo(minute)
        assertThat(store.state.scheduledDate).isEqualTo(newDate)
        // and when
        store.accept(Intent.OnScheduledDateTimeReset)
        assertThat(store.state.scheduledHour).isEqualTo(-1)
        assertThat(store.state.scheduledMinute).isEqualTo(-1)
        assertThat(store.state.scheduledDate).isEqualTo(-1L)
    }

    @Test
    fun `SendStatus should navigate to home screen on success`() = runTest {
        // given
        val bundle = NewStatusBundle.Builder().status("Test").build()
        // when
        store.init()
        store.accept(Intent.SendStatus(bundle))
        // then
        assertThat(labels.count { it is Label.StatusSent }).isEqualTo(1)
    }

    @Test
    fun `SendStatus for scheduled should navigate to scheduled statuses on success`() = runTest {
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
        store.accept(Intent.OnScheduleTime(hour, minute, true))
        store.accept(Intent.OnScheduleDate(newDate))
        store.accept(Intent.SendStatus(bundle))
        // then
        assertThat(labels.count { it is Label.ScheduledStatusSent }).isEqualTo(1)
    }

    @Test
    fun `SendStatus should throw an error on failure`() = runTest {
        // given
        val bundle = NewStatusBundle.Builder().status("Test").build()
        // when
        store.init()
        api.shouldThrowException = true
        store.accept(Intent.SendStatus(bundle))
        // then
        assertThat(labels.count { it is Label.ErrorCaught }).isEqualTo(1)
    }

    override fun createStore(): Store<Intent, State, Label> =
        EditorTabStoreProvider(
            storeFactory = DefaultStoreFactory(),
            manager = manager,
            mainContext = Dispatchers.Unconfined,
            ioContext = Dispatchers.Unconfined,
            today = { todayMock },
        ).create(autoInit = false)
}
