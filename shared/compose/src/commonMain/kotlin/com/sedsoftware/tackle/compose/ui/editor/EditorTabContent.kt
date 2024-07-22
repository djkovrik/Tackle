package com.sedsoftware.tackle.compose.ui.editor

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.sedsoftware.tackle.compose.ui.editor.content.EditorToolbar
import com.sedsoftware.tackle.compose.ui.editor.content.buildToolbarState
import com.sedsoftware.tackle.compose.ui.editor.header.EditorHeaderContent
import com.sedsoftware.tackle.compose.ui.editor.header.content.LanguageSelectorDialog
import com.sedsoftware.tackle.compose.ui.editor.header.content.VisibilitySelectorDialog
import com.sedsoftware.tackle.compose.ui.editor.poll.EditorPollContent
import com.sedsoftware.tackle.compose.ui.editor.warning.EditorWarningContent
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.editor.EditorTabComponent
import com.sedsoftware.tackle.editor.attachments.EditorAttachmentsComponent
import com.sedsoftware.tackle.editor.emojis.EditorEmojisComponent
import com.sedsoftware.tackle.editor.header.EditorHeaderComponent
import com.sedsoftware.tackle.editor.integration.EditorTabComponentPreview
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
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

    Column {
        // Header
        EditorHeaderContent(
            model = headerModel,
            modifier = modifier,
            onLocalePickerRequest = { component.header.onLocalePickerRequested(true) },
            onVisibilityPickerRequest = { component.header.onStatusVisibilityPickerRequested(true) },
        )

        // Content
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = modifier
        ) {

            // Scrollable part
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f, true),
            ) {
                // Input
                item {
                    TextField(
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
                        modifier = modifier.fillMaxWidth()
                    )
                }

                // Warning
                if (warningModel.warningContentVisible) {
                    item {
                        EditorWarningContent(
                            text = warningModel.text,
                            onTextInput = component.warning::onTextInput,
                            onTextClear = component.warning::onClearTextInput,
                            modifier = Modifier,
                        )
                    }
                }

                // Poll
                if (pollModel.pollContentVisible) {
                    item {
                        EditorPollContent(
                            model = pollModel,
                            modifier = modifier,
                            onAddNewItem = component.poll::onAddPollOptionClick,
                            onDeleteItem = component.poll::onDeletePollOptionClick,
                            onMultiselectEnabled = component.poll::onMultiselectEnabled,
                            onDurationSelected = component.poll::onDurationSelected,
                            onTextInput = component.poll::onTextInput,
                            onDurationPickerCall = { component.poll.onDurationPickerRequested(true) },
                            onDurationPickerCancel = { component.poll.onDurationPickerRequested(false) },
                        )
                    }
                }
            }

            // Fixed at bottom
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(horizontal = 8.dp)
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
                    modifier = Modifier.padding(all = 8.dp),
                )
            }
        }

        // Dialogs
        if (headerModel.localePickerDisplayed) {
            LanguageSelectorDialog(
                model = headerModel,
                onDismissRequest = {
                    component.header.onLocalePickerRequested(false)
                },
                onConfirmation = { locale ->
                    component.header.onLocaleSelected(locale)
                    component.header.onLocalePickerRequested(false)
                },
                modifier = modifier,
            )
        }

        if (headerModel.statusVisibilityPickerDisplayed) {
            VisibilitySelectorDialog(
                onDismissRequest = {
                    component.header.onStatusVisibilityPickerRequested(false)
                },
                onConfirmation = { visibility ->
                    component.header.onStatusVisibilitySelected(visibility)
                    component.header.onStatusVisibilityPickerRequested(false)
                },
                modifier = modifier,
            )
        }
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
