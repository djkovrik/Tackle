package com.sedsoftware.tackle.compose.ui.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.ChildSlot
import com.sedsoftware.tackle.compose.model.EditorToolbarItem
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.editor.child.attachment.EditorAttachmentsContent
import com.sedsoftware.tackle.compose.ui.editor.child.details.EditorAttachmentDetailsContent
import com.sedsoftware.tackle.compose.ui.editor.child.emoji.EditorEmojisContent
import com.sedsoftware.tackle.compose.ui.editor.child.header.EditorHeaderContent
import com.sedsoftware.tackle.compose.ui.editor.child.header.content.LanguageSelectorDialog
import com.sedsoftware.tackle.compose.ui.editor.child.header.content.VisibilitySelectorDialog
import com.sedsoftware.tackle.compose.ui.editor.child.poll.EditorPollContent
import com.sedsoftware.tackle.compose.ui.editor.child.warning.EditorWarningContent
import com.sedsoftware.tackle.compose.ui.editor.content.EditorToolbar
import com.sedsoftware.tackle.compose.ui.editor.content.InputHintAccount
import com.sedsoftware.tackle.compose.ui.editor.content.InputHintEmoji
import com.sedsoftware.tackle.compose.ui.editor.content.InputHintHashTag
import com.sedsoftware.tackle.compose.ui.editor.content.ScheduleDatePickerDialog
import com.sedsoftware.tackle.compose.ui.editor.content.ScheduleTimePickerDialog
import com.sedsoftware.tackle.compose.ui.editor.content.ScheduledPostDate
import com.sedsoftware.tackle.compose.ui.editor.content.buildToolbarState
import com.sedsoftware.tackle.compose.widget.TackleTextField
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import com.sedsoftware.tackle.editor.EditorComponent
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.details.EditorAttachmentDetailsComponent
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.integration.EditorComponentPreview
import com.sedsoftware.tackle.editor.model.EditorInputHintItem
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.poll.model.PollChoiceOption
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent
import com.sedsoftware.tackle.utils.TackleRegex
import com.sedsoftware.tackle.utils.extension.orZero
import io.github.vinceglb.filekit.compose.PickerResultLauncher
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.datetime.Clock.System
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_input_hint
import kotlin.time.Duration.Companion.hours

@Composable
internal fun EditorContent(
    component: EditorComponent,
    modifier: Modifier = Modifier,
) {
    val todayDateTime: LocalDateTime by lazy {
        System.now().toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
    }

    val thresholdMillis: Long by lazy {
        System.now().minus(24.hours).toEpochMilliseconds()
    }

    val editorModel: EditorComponent.Model by component.model.subscribeAsState()
    val attachmentsModel: EditorAttachmentsComponent.Model by component.attachments.model.subscribeAsState()
    val emojisModel: EditorEmojisComponent.Model by component.emojis.model.subscribeAsState()
    val headerModel: EditorHeaderComponent.Model by component.header.model.subscribeAsState()
    val pollModel: EditorPollComponent.Model by component.poll.model.subscribeAsState()
    val warningModel: EditorWarningComponent.Model by component.warning.model.subscribeAsState()

    val attachmentDetailsSlot: ChildSlot<*, EditorAttachmentDetailsComponent> by component.attachmentDetailsDialog.subscribeAsState()

    val launcher: PickerResultLauncher = rememberFilePickerLauncher(mode = PickerMode.Multiple()) { files: List<PlatformFile>? ->
        files?.let { component.attachments.onFilesSelected(it) }
    }

    val bottomSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val datePickerState: DatePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
        initialSelectedDateMillis = editorModel.scheduledDate.takeIf { it > 0L },
        selectableDates = object : SelectableDates {
            override fun isSelectableYear(year: Int): Boolean {
                val todayYear = todayDateTime.year
                val max = todayYear + 3
                return year in todayYear..max
            }

            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= thresholdMillis
            }
        }
    )

    val timePickerState: TimePickerState = rememberTimePickerState(
        initialHour = editorModel.scheduledHour,
        initialMinute = editorModel.scheduledMinute,
        is24Hour = editorModel.scheduledIn24hrFormat,
    )

    val annotatedText: AnnotatedString = buildAnnotatedString {
        val regex: Regex = TackleRegex.hashtagAndMentions
        var lastIndex = 0

        regex.findAll(editorModel.statusText).forEach { result ->
            append(editorModel.statusText.substring(lastIndex, result.range.first))
            pushStringAnnotation(tag = "USER_TAG", annotation = result.value)

            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Medium,
                )
            ) {
                append(result.value)
            }

            pop()

            lastIndex = result.range.last + 1
        }

        append(editorModel.statusText.substring(lastIndex))
    }

    Scaffold(
        modifier = modifier.imePadding(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { scaffoldPadding: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = scaffoldPadding)
                .consumeWindowInsets(paddingValues = scaffoldPadding)
                .systemBarsPadding(),
        ) {
            // Header
            EditorHeaderContent(
                model = headerModel,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onLocalePickerRequest = { component.header.onLocalePickerRequested(true) },
                onVisibilityPickerRequest = { component.header.onStatusVisibilityPickerRequested(true) },
                onSendClick = { component.onSendButtonClicked() },
                onBackClick = { component.onBackButtonClicked() },
            )

            AnimatedVisibility(visible = editorModel.sending) {
                LinearProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                )
            }

            // Scrollable part
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                modifier = modifier.weight(weight = 1f, fill = true),
            ) {
                // Warning
                item {
                    AnimatedVisibility(visible = warningModel.warningContentVisible) {
                        EditorWarningContent(
                            text = warningModel.text,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                            onTextInput = component.warning::onTextInput,
                            onTextClear = component.warning::onClearTextInput,
                        )
                    }
                }

                item {
                    // Scheduled post
                    AnimatedVisibility(visible = editorModel.scheduledDateLabel.isNotEmpty()) {
                        ScheduledPostDate(
                            text = editorModel.scheduledDateLabel,
                            onClose = component::resetScheduledDateTime,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                        )
                    }
                }

                // Input
                item {
                    TackleTextField(
                        value = TextFieldValue(
                            annotatedString = annotatedText,
                            selection = TextRange(
                                start = editorModel.statusTextSelection.first,
                                end = editorModel.statusTextSelection.second
                            ),
                        ),
                        onValueChange = { component.onTextInput(it.text, it.selection.min to it.selection.max) },
                        maxLines = 6,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        placeholder = {
                            Text(
                                text = stringResource(resource = Res.string.editor_input_hint),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.5f,
                                ),
                            )
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                            .fillMaxWidth()
                    )
                }

                // Attachments
                item {
                    AnimatedVisibility(visible = attachmentsModel.attachmentsContentVisible) {
                        EditorAttachmentsContent(
                            model = attachmentsModel,
                            onDelete = component.attachments::onFileDeleted,
                            onRetry = component.attachments::onFileRetry,
                            onEdit = component.attachments::onFileEdit,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                }

                // Poll
                item {
                    AnimatedVisibility(visible = pollModel.pollContentVisible) {
                        EditorPollContent(
                            model = pollModel,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            checkboxColor = MaterialTheme.colorScheme.secondary,
                            onAddNewItem = component.poll::onAddPollOptionClick,
                            onDeleteItem = component.poll::onDeletePollOptionClick,
                            onMultiselectEnabled = component.poll::onMultiselectEnabled,
                            onHideTotalsEnabled = component.poll::onHideTotalsEnabled,
                            onDurationSelected = component.poll::onDurationSelected,
                            onTextInput = component.poll::onTextInput,
                            onDurationPickerCall = { component.poll.onDurationPickerRequested(true) },
                            onDurationPickerClose = { component.poll.onDurationPickerRequested(false) },
                        )
                    }
                }
            }

            // Hints
            AnimatedVisibility(visible = editorModel.suggestions.isNotEmpty()) {
                LazyRow(
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    items(
                        items = editorModel.suggestions,
                        key = { it.hashCode() },
                    ) { item: EditorInputHintItem ->
                        when (item) {
                            is EditorInputHintItem.Account ->
                                InputHintAccount(
                                    hint = item,
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    onClick = { component.onInputHintSelected(item) },
                                )

                            is EditorInputHintItem.Emoji ->
                                InputHintEmoji(
                                    hint = item,
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    onClick = { component.onInputHintSelected(item) },
                                )

                            is EditorInputHintItem.HashTag ->
                                InputHintHashTag(
                                    hint = item,
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    onClick = { component.onInputHintSelected(item) },
                                )
                        }
                    }
                }
            }


            // Toolbar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp),
            ) {
                // Toolbar
                EditorToolbar(
                    items = buildToolbarState(editorModel, attachmentsModel, emojisModel, pollModel, warningModel),
                    onClick = { type: EditorToolbarItem.Type ->
                        when (type) {
                            EditorToolbarItem.Type.ATTACH -> launcher.launch()
                            EditorToolbarItem.Type.EMOJIS -> component.onEmojisButtonClicked()
                            EditorToolbarItem.Type.POLL -> component.onPollButtonClicked()
                            EditorToolbarItem.Type.WARNING -> component.onWarningButtonClicked()
                            EditorToolbarItem.Type.SCHEDULE -> component.onScheduleDatePickerRequested(true)
                        }
                    },
                    modifier = Modifier.padding(start = 4.dp),
                )

                Spacer(modifier = Modifier.weight(1f, true))

                // Counter
                Text(
                    text = "${editorModel.statusCharactersLeft}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(end = 16.dp),
                )
            }
        }
    }

    // Locale dialog
    if (headerModel.localePickerDisplayed) {
        LanguageSelectorDialog(
            model = headerModel,
            modifier = Modifier,
            onDismissRequest = {
                component.header.onLocalePickerRequested(false)
            },
            onConfirmation = { locale: AppLocale ->
                component.header.onLocaleSelected(locale)
                component.header.onLocalePickerRequested(false)
            },
        )
    }

    // Visibility dialog
    if (headerModel.statusVisibilityPickerDisplayed) {
        VisibilitySelectorDialog(
            modifier = Modifier,
            onDismissRequest = {
                component.header.onStatusVisibilityPickerRequested(false)
            },
            onConfirmation = { visibility: StatusVisibility ->
                component.header.onStatusVisibilitySelected(visibility)
                component.header.onStatusVisibilityPickerRequested(false)
            },
        )
    }

    // Date picker dialog
    if (editorModel.datePickerVisible) {
        ScheduleDatePickerDialog(
            datePickerState = datePickerState,
            onDismissRequest = {
                component.onScheduleDatePickerRequested(false)
            },
            onConfirmation = {
                component.onScheduleDateSelected(datePickerState.selectedDateMillis.orZero())
                component.onScheduleDatePickerRequested(false)
                component.onScheduleTimePickerRequested(true)
            }
        )
    }

    // Time picker dialog
    if (editorModel.timePickerVisible) {
        ScheduleTimePickerDialog(
            timePickerState = timePickerState,
            onDismissRequest = {
                component.onScheduleTimePickerRequested(false)
            },
            onConfirmation = {
                component.onScheduleTimeSelected(timePickerState.hour, timePickerState.minute, timePickerState.is24hour)
                component.onScheduleTimePickerRequested(false)
            }
        )
    }


    // Emoji bottom sheet
    if (emojisModel.emojisContentVisible) {
        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = component::onEmojisButtonClicked,
            sheetState = bottomSheetState,
        ) {
            EditorEmojisContent(
                emojis = emojisModel.emojis,
                modifier = Modifier.height(height = 360.dp),
                onClick = component.emojis::onEmojiClicked,
            )
        }
    }

    AnimatedVisibility(
        visible = attachmentDetailsSlot.child?.instance != null,
        enter = slideInHorizontally { it },
        exit = slideOutHorizontally { it },
    ) {
        val rememberedComponent = remember(this) { attachmentDetailsSlot.child?.instance!! }
        EditorAttachmentDetailsContent(component = rememberedComponent)
    }
}

@Preview
@Composable
private fun EditorTabContentPreviewIdle() {
    TackleScreenPreview {
        EditorContent(
            component = EditorComponentPreview(
                attachmentsButtonAvailable = true,
                emojisButtonAvailable = true,
                pollButtonAvailable = true,
                avatar = "https://mastodon.social/avatars/original/missing.png",
                nickname = "djkovrik",
                domain = "mastodon.social",
                selectedLocale = AppLocale("English", "en"),
                statusCharactersLeft = 123,
            )
        )
    }
}

@Preview
@Composable
private fun EditorTabContentPreviewWarning() {
    TackleScreenPreview {
        EditorContent(
            component = EditorComponentPreview(
                attachmentsButtonAvailable = true,
                emojisButtonAvailable = true,
                pollButtonAvailable = true,
                avatar = "https://mastodon.social/avatars/original/missing.png",
                nickname = "djkovrik",
                domain = "mastodon.social",
                selectedLocale = AppLocale("English", "en"),
                statusCharactersLeft = 123,
                warningContentVisible = true,
            )
        )
    }
}

@Preview
@Composable
private fun EditorTabContentPreviewAttachments() {
    val platformFile = PlatformFileWrapper("", "", "", "", 0L, "123 Mb") { ByteArray(0) }

    TackleScreenPreview {
        EditorContent(
            component = EditorComponentPreview(
                emojisButtonAvailable = true,
                pollButtonAvailable = true,
                avatar = "https://mastodon.social/avatars/original/missing.png",
                nickname = "djkovrik",
                domain = "mastodon.social",
                selectedLocale = AppLocale("English", "en"),
                statusCharactersLeft = 123,
                attachmentsContentVisible = true,
                attachmentsButtonAvailable = false,
                attachments = listOf(
                    AttachedFile("id", platformFile.copy(mimeType = "audio"), AttachedFile.Status.LOADING, 0.25f),
                    AttachedFile("id", platformFile.copy(mimeType = "image"), AttachedFile.Status.LOADED, 1f),
                    AttachedFile("id", platformFile.copy(mimeType = "video"), AttachedFile.Status.PENDING, 0f),
                    AttachedFile("id", platformFile.copy(mimeType = "unknown"), AttachedFile.Status.ERROR, 1f),
                )
            )
        )
    }
}

@Preview
@Composable
private fun EditorTabContentPreviewPoll() {
    TackleScreenPreview {
        EditorContent(
            component = EditorComponentPreview(
                attachmentsButtonAvailable = false,
                emojisButtonAvailable = true,
                avatar = "https://mastodon.social/avatars/original/missing.png",
                nickname = "djkovrik",
                domain = "mastodon.social",
                selectedLocale = AppLocale("English", "en"),
                statusCharactersLeft = 123,
                pollOptions = listOf(
                    PollChoiceOption(id = "1", text = "Some text here"),
                    PollChoiceOption(id = "2", text = "Another text here"),
                    PollChoiceOption(id = "3", text = ""),
                ),
                multiselectEnabled = true,
                duration = PollDuration.ONE_DAY,
                insertionAvailable = true,
                deletionAvailable = true,
                pollButtonAvailable = true,
                pollContentVisible = true,
            )
        )
    }
}

@Preview
@Composable
private fun EditorTabContentAccountAndHashTagPreview() {
    TackleScreenPreview {
        EditorContent(
            component = EditorComponentPreview(
                attachmentsButtonAvailable = true,
                emojisButtonAvailable = true,
                pollButtonAvailable = true,
                avatar = "https://mastodon.social/avatars/original/missing.png",
                nickname = "djkovrik",
                domain = "mastodon.social",
                selectedLocale = AppLocale("English", "en"),
                statusCharactersLeft = 123,
                statusText = "This is @mention and #hashTag here.",
                suggestions = listOf(
                    EditorInputHintItem.HashTag("Test"),
                    EditorInputHintItem.HashTag("Tag"),
                    EditorInputHintItem.HashTag("Stop"),
                )
            )
        )
    }
}

@Preview
@Composable
private fun EditorTabContentPreviewEverything() {
    val platformFile = PlatformFileWrapper("", "", "", "", 0L, "123 Mb") { ByteArray(0) }

    TackleScreenPreview {
        EditorContent(
            component = EditorComponentPreview(
                emojisButtonAvailable = true,
                avatar = "https://mastodon.social/avatars/original/missing.png",
                nickname = "djkovrik",
                domain = "mastodon.social",
                selectedLocale = AppLocale("English", "en"),
                statusCharactersLeft = 123,
                suggestions = listOf(
                    EditorInputHintItem.HashTag("Test"),
                    EditorInputHintItem.HashTag("Tag"),
                    EditorInputHintItem.HashTag("Stop"),
                ),
                pollOptions = listOf(
                    PollChoiceOption(id = "1", text = "Some text here"),
                    PollChoiceOption(id = "2", text = "Another text here"),
                    PollChoiceOption(id = "3", text = ""),
                ),
                multiselectEnabled = true,
                duration = PollDuration.ONE_DAY,
                insertionAvailable = true,
                deletionAvailable = true,
                pollButtonAvailable = true,
                pollContentVisible = true,
                warningContentVisible = true,
                attachmentsContentVisible = true,
                attachmentsButtonAvailable = true,
                attachments = listOf(
                    AttachedFile("id", platformFile.copy(mimeType = "audio"), AttachedFile.Status.LOADING, 0.25f),
                ),
                scheduledDateLabel = "30.08.2024 12:34",
                sendingActive = true,
            )
        )
    }
}
