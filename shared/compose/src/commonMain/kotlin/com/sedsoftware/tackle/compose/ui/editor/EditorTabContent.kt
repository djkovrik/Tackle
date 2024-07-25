package com.sedsoftware.tackle.compose.ui.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.sedsoftware.tackle.compose.model.EditorToolbarItem
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.editor.attachment.EditorAttachmentsContent
import com.sedsoftware.tackle.compose.ui.editor.content.EditorToolbar
import com.sedsoftware.tackle.compose.ui.editor.content.buildToolbarState
import com.sedsoftware.tackle.compose.ui.editor.emoji.EditorEmojisContent
import com.sedsoftware.tackle.compose.ui.editor.header.EditorHeaderContent
import com.sedsoftware.tackle.compose.ui.editor.header.content.LanguageSelectorDialog
import com.sedsoftware.tackle.compose.ui.editor.header.content.VisibilitySelectorDialog
import com.sedsoftware.tackle.compose.ui.editor.poll.EditorPollContent
import com.sedsoftware.tackle.compose.ui.editor.warning.EditorWarningContent
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.domain.model.PlatformFileWrapper
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.attachments.model.AttachedFile
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.integration.EditorTabComponentPreview
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.poll.model.PollChoiceOption
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.warning.EditorWarningComponent
import io.github.vinceglb.filekit.compose.PickerResultLauncher
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PlatformFile
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_input_hint

@Composable
internal fun EditorTabContent(
    component: EditorTabComponent,
    modifier: Modifier = Modifier,
) {
    val editorModel: EditorTabComponent.Model by component.model.subscribeAsState()
    val attachmentsModel: EditorAttachmentsComponent.Model by component.attachments.model.subscribeAsState()
    val emojisModel: EditorEmojisComponent.Model by component.emojis.model.subscribeAsState()
    val headerModel: EditorHeaderComponent.Model by component.header.model.subscribeAsState()
    val pollModel: EditorPollComponent.Model by component.poll.model.subscribeAsState()
    val warningModel: EditorWarningComponent.Model by component.warning.model.subscribeAsState()

    val launcher: PickerResultLauncher = rememberFilePickerLauncher(mode = PickerMode.Multiple()) { files: List<PlatformFile>? ->
        files?.let { component.attachments.onFilesSelected(it) }
    }

    val bottomSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column {
        // Header
        EditorHeaderContent(
            model = headerModel,
            modifier = modifier,
            onLocalePickerRequest = { component.header.onLocalePickerRequested(true) },
            onVisibilityPickerRequest = { component.header.onStatusVisibilityPickerRequested(true) },
        )

        // Scrollable part
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            modifier = modifier.weight(1f, true),
        ) {
            // Warning
            item {
                if (warningModel.warningContentVisible) {
                    EditorWarningContent(
                        text = warningModel.text,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onTextInput = component.warning::onTextInput,
                        onTextClear = component.warning::onClearTextInput,
                    )
                }
            }

            // Input
            item {
                OutlinedTextField(
                    value = TextFieldValue(
                        text = editorModel.statusText,
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
                            text = stringResource(Res.string.editor_input_hint),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.5f,
                            ),
                        )
                    },
                    shape = RoundedCornerShape(size = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    ),
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                        .fillMaxWidth()
                )
            }

            // Toolbar
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    // Toolbar
                    EditorToolbar(
                        items = buildToolbarState(attachmentsModel, emojisModel, pollModel, warningModel),
                        onClick = { type: EditorToolbarItem.Type ->
                            when (type) {
                                EditorToolbarItem.Type.ATTACH -> launcher.launch()
                                EditorToolbarItem.Type.EMOJIS -> component.onEmojisButtonClicked()
                                EditorToolbarItem.Type.POLL -> component.onPollButtonClicked()
                                EditorToolbarItem.Type.WARNING -> component.onWarningButtonClicked()
                                EditorToolbarItem.Type.SCHEDULE -> TODO()
                            }
                        },
                        modifier = Modifier,
                    )

                    Spacer(modifier = Modifier.weight(1f, true))

                    // Counter
                    Text(
                        text = "${editorModel.statusCharactersLeft}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier,
                    )
                }
            }

            // Attachments
            item {
                AnimatedVisibility(visible = attachmentsModel.attachmentsContentVisible) {
                    EditorAttachmentsContent(
                        model = attachmentsModel,
                        onDelete = component.attachments::onFileDeleted,
                        onRetry = component.attachments::onFileRetry,
                        modifier = Modifier,
                    )
                }
            }

            // Poll
            item {
                AnimatedVisibility(visible = pollModel.pollContentVisible) {
                    EditorPollContent(
                        model = pollModel,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 8.dp, bottom = 16.dp),
                        onAddNewItem = component.poll::onAddPollOptionClick,
                        onDeleteItem = component.poll::onDeletePollOptionClick,
                        onMultiselectEnabled = component.poll::onMultiselectEnabled,
                        onDurationSelected = component.poll::onDurationSelected,
                        onTextInput = component.poll::onTextInput,
                        onDurationPickerCall = { component.poll.onDurationPickerRequested(true) },
                        onDurationPickerClose = { component.poll.onDurationPickerRequested(false) },
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
                onConfirmation = { locale ->
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
                onConfirmation = { visibility ->
                    component.header.onStatusVisibilitySelected(visibility)
                    component.header.onStatusVisibilityPickerRequested(false)
                },
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
    }
}

@Preview
@Composable
private fun EditorTabContentPreviewEverything() {
    val platformFile = PlatformFileWrapper("", "", "", "", 0L, "123 Mb") { ByteArray(0) }

    TackleScreenPreview {
        EditorTabContent(
            component = EditorTabComponentPreview(
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
                warningContentVisible = true,
                attachmentsContentVisible = true,
                attachmentsButtonAvailable = true,
                attachments = listOf(
                    AttachedFile("id", platformFile.copy(mimeType = "audio"), AttachedFile.Status.LOADING, 25),
                    AttachedFile("id", platformFile.copy(mimeType = "video"), AttachedFile.Status.ERROR, 100),
                )
            )
        )
    }
}

@Preview
@Composable
private fun EditorTabContentPreviewIdle() {
    TackleScreenPreview {
        EditorTabContent(
            component = EditorTabComponentPreview(
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
private fun EditorTabContentPreviewAttachments() {
    val platformFile = PlatformFileWrapper("", "", "", "", 0L, "123 Mb") { ByteArray(0) }

    TackleScreenPreview {
        EditorTabContent(
            component = EditorTabComponentPreview(
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
                    AttachedFile("id", platformFile.copy(mimeType = "audio"), AttachedFile.Status.LOADING, 25),
                    AttachedFile("id", platformFile.copy(mimeType = "image"), AttachedFile.Status.LOADED, 100),
                    AttachedFile("id", platformFile.copy(mimeType = "video"), AttachedFile.Status.PENDING, 0),
                    AttachedFile("id", platformFile.copy(mimeType = "unknown"), AttachedFile.Status.ERROR, 100),
                )
            )
        )
    }
}

@Preview
@Composable
private fun EditorTabContentPreviewPoll() {
    TackleScreenPreview {
        EditorTabContent(
            component = EditorTabComponentPreview(
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
private fun EditorTabContentPreviewWarning() {
    TackleScreenPreview {
        EditorTabContent(
            component = EditorTabComponentPreview(
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
