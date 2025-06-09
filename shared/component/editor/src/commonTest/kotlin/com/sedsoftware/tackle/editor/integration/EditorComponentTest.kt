package com.sedsoftware.tackle.editor.integration

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.child
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.sedsoftware.tackle.domain.ComponentOutput
import com.sedsoftware.tackle.domain.model.CustomEmoji
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.EditorComponent
import com.sedsoftware.tackle.editor.EditorComponentGateways
import com.sedsoftware.tackle.editor.Instances
import com.sedsoftware.tackle.editor.Responses
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent
import com.sedsoftware.tackle.editor.details.integration.EditorAttachmentDetailsComponentDefault
import com.sedsoftware.tackle.editor.details.model.AttachmentParams
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.integration.EditorComponentDefault.AttachmentDetailsConfig
import com.sedsoftware.tackle.editor.integration.attachments.EditorAttachmentDetailsComponentApi
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.stubs.EditorComponentSettingsStub
import com.sedsoftware.tackle.editor.stubs.EditorComponentToolsStub
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent
import com.sedsoftware.tackle.utils.test.ComponentTest
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.flow.flowOf
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

    private val context: DefaultComponentContext = DefaultComponentContext(lifecycle)
    private val storeFactory: DefaultStoreFactory = DefaultStoreFactory()
    private val navigation = SlotNavigation<AttachmentDetailsConfig>()
    private val childComponentOutput: MutableList<ComponentOutput> = mutableListOf()

    @BeforeTest
    fun beforeTest() {
        setUp()
    }

    @AfterTest
    fun afterTest() {
        tearDown()
    }

    @Test
    fun `component creation should initialize all available children`() = runTest {
        // given
        // when
        // then
        assertThat(editorActiveModel.statusText).isEmpty()
        assertThat(editorActiveModel.statusTextSelection).isEqualTo(0 to 0)
        assertThat(editorActiveModel.statusCharactersLeft).isEqualTo(Instances.instanceConfig.statuses.maxCharacters)

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
        component.onTextInput(inputText, inputSelection)
        // then
        assertThat(editorActiveModel.statusText).isEqualTo(inputText)
        assertThat(editorActiveModel.statusTextSelection).isEqualTo(inputSelection)
        assertThat(editorActiveModel.statusCharactersLeft).isEqualTo(Instances.instanceConfig.statuses.maxCharacters - inputText.length)
    }

    @Test
    fun `onEmojiSelect should update editor text`() = runTest {
        // given
        val emoji = CustomEmoji(":cool:", "", "", true, "")
        // when
        component.onEmojiSelect(emoji)
        // then
        assertThat(editorActiveModel.statusText).contains(emoji.shortcode)
    }

    @Test
    fun `onPollButtonClick should display poll and update buttons`() = runTest {
        // given
        // when
        component.onPollButtonClick()
        // then
        assertThat(pollActiveModel.pollButtonAvailable).isTrue()
        assertThat(pollActiveModel.pollContentVisible).isTrue()
        assertThat(attachmentsActiveModel.attachmentsButtonAvailable).isFalse()
        // when
        component.onPollButtonClick()
        // then
        assertThat(pollActiveModel.pollButtonAvailable).isTrue()
        assertThat(pollActiveModel.pollContentVisible).isFalse()
        assertThat(attachmentsActiveModel.attachmentsButtonAvailable).isTrue()
    }

    @Test
    fun `onEmojisButtonClick should display emojis and update buttons`() = runTest {
        // given
        // when
        component.onEmojisButtonClick()
        // then
        assertThat(emojisActiveModel.emojisContentVisible).isTrue()
        // when
        component.onEmojisButtonClick()
        // then
        assertThat(emojisActiveModel.emojisContentVisible).isFalse()
    }

    @Test
    fun `onInputHintSelect updates input state`() = runTest {
        // given
        val inputText = "Some text and test #hash"
        val inputTextSelection = inputText.length to inputText.length
        val expectedText = "Some text and test #hashtag"
        val hint = EditorInputHintItem.HashTag("hashtag")
        // when
        component.onTextInput(inputText, inputTextSelection)
        component.onInputHintSelect(hint)
        // then
        assertThat(editorActiveModel.statusText).isEqualTo(expectedText)
    }

    @Test
    fun `onWarningButtonClick should display warning and update buttons`() = runTest {
        // given
        // when
        component.onWarningButtonClick()
        // then
        assertThat(warningActiveModel.warningContentVisible).isTrue()
        // when
        component.onWarningButtonClick()
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
        component.attachments.onWrappedFilesSelect(files)

        // then
        assertThat(attachmentsActiveModel.attachmentsContentVisible).isTrue()
        assertThat(pollActiveModel.pollButtonAvailable).isFalse()
    }

    @Test
    fun `onScheduleDatePickerRequest should control date picker`() = runTest {
        // given
        // when
        component.onScheduleDatePickerRequest(true)
        // then
        assertThat(editorActiveModel.datePickerVisible).isTrue()
        // and when
        component.onScheduleDatePickerRequest(false)
        // then
        assertThat(editorActiveModel.datePickerVisible).isFalse()
    }

    @Test
    fun `onScheduleDateSelect should update scheduled date`() = runTest {
        // given
        val newDate = 54321L
        // when
        component.onScheduleDateSelect(newDate)
        // then
        assertThat(editorActiveModel.scheduledDate).isEqualTo(newDate)
    }

    @Test
    fun `onScheduleTimePickerRequest should control time picker`() = runTest {
        // given
        // when
        component.onScheduleTimePickerRequest(true)
        // then
        assertThat(editorActiveModel.timePickerVisible).isTrue()
        // and when
        component.onScheduleTimePickerRequest(false)
        // then
        assertThat(editorActiveModel.timePickerVisible).isFalse()
    }

    @Test
    fun `onScheduleTimeSelect should update scheduled date`() = runTest {
        // given
        val hour = 16
        val minute = 54
        val format = false
        // when
        component.onScheduleTimeSelect(hour, minute, format)
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
        component.onScheduleDateSelect(newDate)
        component.onScheduleTimeSelect(hour, minute, true)
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
    fun `onSendButtonClick should send new status`() = runTest {
        // given
        val text = ""
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

        component.poll.onTextInput(pollActiveModel.options[0].id, "Text 1")
        component.poll.onTextInput(pollActiveModel.options[1].id, "Text 2")
        component.poll.toggleComponentVisibility()

        component.attachments.onWrappedFilesSelect(files)
        // when
        component.onSendButtonClick()
        // then
        assertThat(editorActiveModel.statusText).isEmpty()
    }

    @Test
    fun `onSendButtonClick with api error should show error message`() = runTest {
        // given
        val text = "Status text"
        component.onTextInput(text, text.length to text.length)
        everySuspend { api.sendStatus(any()) } throws IllegalStateException("Test")

        // when
        component.onSendButtonClick()
        // then
        assertThat(componentOutput).isNotEmpty()
    }

    @Test
    fun `onSendButtonClick should send new scheduled status`() = runTest {
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
        component.attachments.onWrappedFilesSelect(files)
        component.onScheduleDateSelect(1231819497600000L)
        component.onScheduleTimeSelect(12, 12, true)
        // when
        component.onSendButtonClick()
        // then
        assertThat(editorActiveModel.statusText).isEmpty()
    }

    @Test
    fun `onBackButtonClick should call back button output`() = runTest {
        // given
        // when
        component.onBackButtonClick()
        // then
        assertThat(componentOutput).contains(ComponentOutput.StatusEditor.BackButtonClicked)
    }

    @Test
    fun `attachment details requests shows dialog`() = runTest {
        // given
        val attachment = Instances.fileToAttach

        val emptySlot = context.childSlot(config = null)
        val dialogSlot = context.childSlot(
            config = AttachmentDetailsConfig(
                attachmentId = attachment.id,
                attachmentType = attachment.type,
                attachmentUrl = attachment.url,
                attachmentImageParams = AttachmentParams(
                    width = attachment.meta!!.width,
                    height = attachment.meta!!.height,
                    ratio = attachment.meta!!.aspect,
                    blurhash = attachment.blurhash,
                ),
            )
        )
        // when
        assertThat(component.attachmentDetailsDialog.child).isEqualTo(emptySlot.child)
        component.attachments.onFileEdit(attachment)
        // then
        assertThat(component.attachmentDetailsDialog.child?.configuration).isEqualTo(dialogSlot.child?.configuration)
    }

    private fun ComponentContext.childSlot(config: AttachmentDetailsConfig?): Value<ChildSlot<*, EditorAttachmentDetailsComponent>> =
        childSlot(
            source = navigation,
            serializer = null,
            handleBackButton = true,
            initialConfiguration = { config },
            childFactory = ::component,
            key = config?.attachmentId ?: "key",
        )

    private fun component(
        config: AttachmentDetailsConfig,
        componentContext: ComponentContext,
    ): EditorAttachmentDetailsComponent =
        EditorAttachmentDetailsComponentDefault(
            attachmentType = config.attachmentType,
            attachmentUrl = config.attachmentUrl,
            attachmentId = config.attachmentId,
            attachmentImageParams = config.attachmentImageParams,
            componentContext = componentContext,
            storeFactory = storeFactory,
            api = EditorAttachmentDetailsComponentApi(api),
            dispatchers = testDispatchers,
            output = { childComponentOutput.add(it) },
            onDismiss = navigation::dismiss,
        )

    override fun createComponent(): EditorComponentDefault =
        EditorComponentDefault(
            componentContext = context,
            storeFactory = storeFactory,
            api = api,
            database = database,
            settings = EditorComponentSettingsStub(),
            tools = tools,
            dispatchers = testDispatchers,
            editorOutput = { componentOutput.add(it) },
        )
}
