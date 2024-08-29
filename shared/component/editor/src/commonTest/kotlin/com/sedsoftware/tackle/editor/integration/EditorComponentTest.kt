package com.sedsoftware.tackle.editor.integration

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.EditorComponent
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.stubs.EditorComponentApiStub
import com.sedsoftware.tackle.editor.stubs.EditorComponentDatabaseStub
import com.sedsoftware.tackle.editor.stubs.EditorComponentSettingsStub
import com.sedsoftware.tackle.editor.stubs.EditorComponentToolsStub
import com.sedsoftware.tackle.editor.stubs.InstanceStub
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent
import com.sedsoftware.tackle.utils.test.ComponentTest
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class EditorComponentTest : ComponentTest<EditorComponent>() {

    private val editorActiveModel: EditorComponent.Model
        get() = component.model.value

    private val attachmentsActiveModel: EditorAttachmentsComponent.Model
        get() = component.attachments.model.value

    private val emojisActiveModel: EditorEmojisComponent.Model
        get() = component.emojis.model.value

    private val headerActiveModel: EditorHeaderComponent.Model
        get() = component.header.model.value

    private val pollActiveModel: EditorPollComponent.Model
        get() = component.poll.model.value

    private val warningActiveModel: EditorWarningComponent.Model
        get() = component.warning.model.value

    @BeforeTest
    fun before() {
        beforeTest()
    }

    @AfterTest
    fun after() {
        afterTest()
    }

    @Test
    fun `component creation should initialize all available children`() = runTest {
        // given
        // when
        component.attachments.updateInstanceConfig(InstanceStub.config)
        component.poll.updateInstanceConfig(InstanceStub.config)
        // then
        assertThat(editorActiveModel.statusText).isEmpty()
        assertThat(editorActiveModel.statusTextSelection).isEqualTo(0 to 0)
        assertThat(editorActiveModel.statusCharactersLeft).isEqualTo(InstanceStub.config.statuses.maxCharacters)

        assertThat(attachmentsActiveModel.attachmentsButtonAvailable).isTrue()
        assertThat(attachmentsActiveModel.attachmentsContentVisible).isFalse()

        assertThat(emojisActiveModel.emojisButtonAvailable).isTrue()
        assertThat(emojisActiveModel.emojisContentVisible).isFalse()

        assertThat(headerActiveModel.avatar).isNotEmpty()
        assertThat(headerActiveModel.nickname).isNotEmpty()

        assertThat(pollActiveModel.pollButtonAvailable).isTrue()
        assertThat(pollActiveModel.pollContentVisible).isFalse()

        assertThat(warningActiveModel.warningContentVisible).isFalse()
    }

    @Test
    fun `onTextInput should update editor text`() = runTest {
        // given
        val inputText = "Test tests"
        val inputSelection = inputText.length to inputText.length
        // when
        component.attachments.updateInstanceConfig(InstanceStub.config)
        component.poll.updateInstanceConfig(InstanceStub.config)
        component.onTextInput(inputText, inputSelection)
        // then
        assertThat(editorActiveModel.statusText).isEqualTo(inputText)
        assertThat(editorActiveModel.statusTextSelection).isEqualTo(inputSelection)
        assertThat(editorActiveModel.statusCharactersLeft).isEqualTo(InstanceStub.config.statuses.maxCharacters - inputText.length)
    }

    @Test
    fun `onEmojiSelected should update editor text`() = runTest {
        // given
        val emoji = CustomEmoji(":cool:", "", "", true, "")
        // when
        component.attachments.updateInstanceConfig(InstanceStub.config)
        component.poll.updateInstanceConfig(InstanceStub.config)
        component.onEmojiSelected(emoji)
        // then
        assertThat(editorActiveModel.statusText).contains(emoji.shortcode)
    }

    @Test
    fun `onPollButtonClicked should display poll and update buttons`() = runTest {
        // given
        // when
        component.attachments.updateInstanceConfig(InstanceStub.config)
        component.poll.updateInstanceConfig(InstanceStub.config)
        component.onPollButtonClicked()
        // then
        assertThat(pollActiveModel.pollButtonAvailable).isTrue()
        assertThat(pollActiveModel.pollContentVisible).isTrue()
        assertThat(attachmentsActiveModel.attachmentsButtonAvailable).isFalse()
        // when
        component.onPollButtonClicked()
        // then
        assertThat(pollActiveModel.pollButtonAvailable).isTrue()
        assertThat(pollActiveModel.pollContentVisible).isFalse()
        assertThat(attachmentsActiveModel.attachmentsButtonAvailable).isTrue()
    }

    @Test
    fun `onEmojisButtonClicked should display emojis and update buttons`() = runTest {
        // given
        // when
        component.attachments.updateInstanceConfig(InstanceStub.config)
        component.poll.updateInstanceConfig(InstanceStub.config)
        component.onEmojisButtonClicked()
        // then
        assertThat(emojisActiveModel.emojisContentVisible).isTrue()
        // when
        component.onEmojisButtonClicked()
        // then
        assertThat(emojisActiveModel.emojisContentVisible).isFalse()
    }

    @Test
    fun `onInputHintSelected updates input state`() = runTest {
        // given
        val inputText = "Some text and test #hash"
        val inputTextSelection = inputText.length to inputText.length
        val expectedText = "Some text and test #hashtag"
        val hint = EditorInputHintItem.HashTag("hashtag")
        // when
        component.attachments.updateInstanceConfig(InstanceStub.config)
        component.poll.updateInstanceConfig(InstanceStub.config)
        component.onTextInput(inputText, inputTextSelection)
        component.onInputHintSelected(hint)
        // then
        assertThat(editorActiveModel.statusText).isEqualTo(expectedText)
    }

    @Test
    fun `onWarningButtonClicked should display warning and update buttons`() = runTest {
        // given
        // when
        component.attachments.updateInstanceConfig(InstanceStub.config)
        component.poll.updateInstanceConfig(InstanceStub.config)
        component.onWarningButtonClicked()
        // then
        assertThat(warningActiveModel.warningContentVisible).isTrue()
        // when
        component.onWarningButtonClicked()
        // then
        assertThat(warningActiveModel.warningContentVisible).isFalse()
    }

    @Test
    fun `selecting attachments should disable poll button`() = runTest {
        // Given
        val image = PlatformFileWrapper(
            name = "normal.jpg",
            extension = "jpg",
            path = "",
            mimeType = "image/jpeg",
            size = 12345L,
            sizeLabel = "",
            readBytes = { ByteArray(0) },
        )

        val files = listOf(
            image.copy(name = "test1.jpg"),
            image.copy(name = "test2.jpg"),
            image.copy(name = "test3.jpg"),
        )

        // when
        component.attachments.updateInstanceConfig(InstanceStub.config)
        component.poll.updateInstanceConfig(InstanceStub.config)
        component.attachments.onFilesSelectedWrapped(files)

        // then
        assertThat(attachmentsActiveModel.attachmentsContentVisible).isTrue()
        assertThat(pollActiveModel.pollButtonAvailable).isFalse()
    }

    @Test
    fun `onScheduleDatePickerRequested should control date picker`() = runTest {
        // given
        // when
        component.onScheduleDatePickerRequested(true)
        // then
        assertThat(editorActiveModel.datePickerVisible).isTrue()
        // and when
        component.onScheduleDatePickerRequested(false)
        // then
        assertThat(editorActiveModel.datePickerVisible).isFalse()
    }

    @Test
    fun `onScheduleDateSelected should update scheduled date`() = runTest {
        // given
        val newDate = 54321L
        // when
        component.onScheduleDateSelected(newDate)
        // then
        assertThat(editorActiveModel.scheduledDate).isEqualTo(newDate)
    }

    @Test
    fun `onScheduleTimePickerRequested should control time picker`() = runTest {
        // given
        // when
        component.onScheduleTimePickerRequested(true)
        // then
        assertThat(editorActiveModel.timePickerVisible).isTrue()
        // and when
        component.onScheduleTimePickerRequested(false)
        // then
        assertThat(editorActiveModel.timePickerVisible).isFalse()
    }

    @Test
    fun `onScheduleTimeSelected should update scheduled date`() = runTest {
        // given
        val hour = 16
        val minute = 54
        val format = false
        // when
        component.onScheduleTimeSelected(hour, minute, format)
        // then
        assertThat(editorActiveModel.scheduledHour).isEqualTo(hour)
        assertThat(editorActiveModel.scheduledMinute).isEqualTo(minute)
        assertThat(editorActiveModel.scheduledIn24hrFormat).isEqualTo(format)
    }

    @Test
    fun `resetScheduledDateTime should reset scheduled date and time`() = runTest {
        // given
        val newDate = 54321L
        val hour = 16
        val minute = 54
        // when
        // when
        component.onScheduleDateSelected(newDate)
        component.onScheduleTimeSelected(hour, minute, true)
        // then
        assertThat(editorActiveModel.scheduledHour).isEqualTo(hour)
        assertThat(editorActiveModel.scheduledMinute).isEqualTo(minute)
        assertThat(editorActiveModel.scheduledDate).isEqualTo(newDate)
        // and when
        component.resetScheduledDateTime()
        // then
        assertThat(editorActiveModel.scheduledHour).isEqualTo(-1)
        assertThat(editorActiveModel.scheduledMinute).isEqualTo(-1)
        assertThat(editorActiveModel.scheduledDate).isEqualTo(-1L)
    }

    @Test
    fun `onSendButtonClicked should send new status`() = runTest {
        // given
        val text = "Status text"
        component.onTextInput(text, text.length to text.length)
        component.warning.onTextInput("Warning")
        component.warning.toggleComponentVisibility()

        val image = PlatformFileWrapper(
            name = "normal.jpg",
            extension = "jpg",
            path = "",
            mimeType = "image/jpeg",
            size = 12345L,
            sizeLabel = "",
            readBytes = { ByteArray(0) },
        )

        val files = listOf(
            image.copy(name = "test1.jpg"),
        )
        component.attachments.updateInstanceConfig(InstanceStub.config)
        component.poll.updateInstanceConfig(InstanceStub.config)
        component.attachments.onFilesSelectedWrapped(files)
        // when
        component.onSendButtonClicked()
        // then
        assertThat(editorActiveModel.statusText).isEmpty()
        assertThat(attachmentsActiveModel.attachments).isEmpty()
    }

    @Test
    fun `onSendButtonClicked should send new scheduled status`() = runTest {
        // given
        val text = "Status text"
        component.onTextInput(text, text.length to text.length)
        component.warning.onTextInput("Warning")
        component.warning.toggleComponentVisibility()

        val image = PlatformFileWrapper(
            name = "normal.jpg",
            extension = "jpg",
            path = "",
            mimeType = "image/jpeg",
            size = 12345L,
            sizeLabel = "",
            readBytes = { ByteArray(0) },
        )

        val files = listOf(
            image.copy(name = "test1.jpg"),
        )
        component.attachments.updateInstanceConfig(InstanceStub.config)
        component.poll.updateInstanceConfig(InstanceStub.config)
        component.attachments.onFilesSelectedWrapped(files)
        component.onScheduleDateSelected(1231819497600000L)
        component.onScheduleTimeSelected(12, 12, true)
        // when
        component.onSendButtonClicked()
        // then
        assertThat(editorActiveModel.statusText).isEmpty()
        assertThat(attachmentsActiveModel.attachments).isEmpty()
    }

    override fun createComponent(): EditorComponentDefault =
        EditorComponentDefault(
            componentContext = DefaultComponentContext(lifecycle),
            storeFactory = DefaultStoreFactory(),
            api = EditorComponentApiStub(),
            database = EditorComponentDatabaseStub(),
            settings = EditorComponentSettingsStub(),
            tools = EditorComponentToolsStub(),
            dispatchers = testDispatchers,
            editorOutput = {},
        )
}
