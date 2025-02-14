package com.sedsoftware.tackle.compose.ui.editor.child.poll

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.getTitle
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.editor.child.poll.content.CustomPollChoice
import com.sedsoftware.tackle.compose.ui.editor.child.poll.content.CustomPollDurationDialog
import com.sedsoftware.tackle.compose.widget.TackleIconButton
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.poll.model.PollChoiceOption
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_add
import tackle.shared.compose.generated.resources.editor_poll_hide_totals
import tackle.shared.compose.generated.resources.editor_poll_multiple

@Composable
internal fun EditorPollContent(
    model: EditorPollComponent.Model,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    checkboxColor: Color = MaterialTheme.colorScheme.primary,
    onAddNewItem: () -> Unit = {},
    onDeleteItem: (String) -> Unit = {},
    onMultiselectEnabled: (Boolean) -> Unit = {},
    onHideTotalsEnabled: (Boolean) -> Unit = {},
    onDurationSelected: (PollDuration) -> Unit = {},
    onTextInput: (String, String) -> Unit = { _, _ -> },
    onDurationPickerCall: () -> Unit = {},
    onDurationPickerClose: () -> Unit = {},
) {
    Column(modifier = modifier) {
        model.options.forEachIndexed { index, pollOption ->
            CustomPollChoice(
                index = index,
                option = pollOption,
                modifier = Modifier,
                deletionAvailable = model.deletionAvailable,
                onTextInput = onTextInput,
                onDelete = { onDeleteItem.invoke(pollOption.id) },
            )
        }

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            // Add icon
            TackleIconButton(
                iconRes = Res.drawable.editor_add,
                enabled = model.insertionAvailable,
                containerColor = containerColor,
                contentColor = contentColor,
                borderWidth = 1.dp,
                modifier = Modifier,
                onClick = { if (model.insertionAvailable) onAddNewItem.invoke() },
            )

            Spacer(modifier = Modifier.width(width = 16.dp))

            // Duration picker
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(height = 42.dp)
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .clickable(onClick = onDurationPickerCall)
                    .background(
                        color = containerColor,
                        shape = MaterialTheme.shapes.extraLarge
                    ),
            ) {
                Text(
                    text = stringResource(resource = model.duration.getTitle()),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        // Multi choice option
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = model.multiselectEnabled,
                onCheckedChange = onMultiselectEnabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = checkboxColor,
                    uncheckedColor = checkboxColor,
                ),
                modifier = Modifier
            )

            Text(
                text = stringResource(resource = Res.string.editor_poll_multiple),
                color = checkboxColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }

        // Hide totals option
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = model.hideTotalsEnabled,
                onCheckedChange = onHideTotalsEnabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = checkboxColor,
                    uncheckedColor = checkboxColor,
                ),
                modifier = Modifier
            )

            Text(
                text = stringResource(resource = Res.string.editor_poll_hide_totals),
                color = checkboxColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }

    // Dialog
    if (model.durationPickerVisible) {
        CustomPollDurationDialog(
            model = model,
            modifier = Modifier,
            onDismiss = onDurationPickerClose,
            onConfirmation = onDurationSelected,
        )
    }
}

@Preview
@Composable
private fun EditorPollContentPreviewTwoOptions() {
    TackleScreenPreview {
        EditorPollContent(
            model = EditorPollComponent.Model(
                options = listOf(
                    PollChoiceOption(id = "1", text = "Some text here"),
                    PollChoiceOption(id = "2", text = "Another text here"),
                ),
                multiselectEnabled = false,
                hideTotalsEnabled = false,
                duration = PollDuration.FIVE_MINUTES,
                availableDurations = emptyList(),
                durationPickerVisible = false,
                insertionAvailable = true,
                deletionAvailable = false,
                pollButtonAvailable = false,
                pollContentVisible = false,
            )
        )
    }
}

@Preview
@Composable
private fun EditorPollContentPreviewThreeOptions() {
    TackleScreenPreview {
        EditorPollContent(
            model = EditorPollComponent.Model(
                options = listOf(
                    PollChoiceOption(id = "1", text = "Some text here"),
                    PollChoiceOption(id = "2", text = "Another text here"),
                    PollChoiceOption(id = "3", text = ""),
                ),
                multiselectEnabled = false,
                hideTotalsEnabled = false,
                duration = PollDuration.FIVE_MINUTES,
                availableDurations = emptyList(),
                durationPickerVisible = false,
                insertionAvailable = true,
                deletionAvailable = true,
                pollButtonAvailable = false,
                pollContentVisible = false,
            )
        )
    }
}
