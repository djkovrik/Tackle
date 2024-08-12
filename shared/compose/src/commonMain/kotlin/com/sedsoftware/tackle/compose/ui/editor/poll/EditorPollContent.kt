package com.sedsoftware.tackle.compose.ui.editor.poll

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.getTitle
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.editor.poll.content.CustomPollChoice
import com.sedsoftware.tackle.compose.ui.editor.poll.content.CustomPollDurationDialog
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.poll.model.PollChoiceOption
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_close
import tackle.shared.compose.generated.resources.editor_poll_multiple

@Composable
internal fun EditorPollContent(
    model: EditorPollComponent.Model,
    modifier: Modifier = Modifier,
    onAddNewItem: () -> Unit = {},
    onDeleteItem: (String) -> Unit = {},
    onMultiselectEnabled: (Boolean) -> Unit = {},
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
                modifier = Modifier.padding(vertical = 4.dp),
                deletionAvailable = model.deletionAvailable,
                onTextInput = onTextInput,
                onDelete = { onDeleteItem.invoke(pollOption.id) },
            )
        }

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            // Add icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(size = 48.dp)
                    .clip(shape = CircleShape)
                    .clickable(onClick = { if (model.insertionAvailable) onAddNewItem.invoke() })
                    .background(color = MaterialTheme.colorScheme.secondaryContainer, shape = CircleShape),
            ) {
                Icon(
                    painter = painterResource(resource = Res.drawable.editor_close),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .size(size = 20.dp)
                        .rotate(degrees = 45f)
                        .alpha(alpha = if (model.insertionAvailable) 1f else 0.5f)
                )
            }

            Spacer(modifier = Modifier.width(width = 16.dp))

            // Duration picker
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(height = 48.dp)
                    .clip(shape = RoundedCornerShape(size = 32.dp))
                    .clickable(onClick = onDurationPickerCall)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(size = 32.dp)),
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
        Row {
            Checkbox(
                checked = model.multiselectEnabled,
                onCheckedChange = onMultiselectEnabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.secondary,
                    uncheckedColor = MaterialTheme.colorScheme.secondary,
                ),
                modifier = Modifier
            )

            Text(
                text = stringResource(resource = Res.string.editor_poll_multiple),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }

    // Dialog
    if (model.durationPickerVisible) {
        CustomPollDurationDialog(
            model = model,
            onDismiss = onDurationPickerClose,
            onConfirmation = onDurationSelected,
            modifier = Modifier,
        )
    }
}

@Preview
@Composable
private fun EditorPollContentPreview() {
    TackleScreenPreview {
        EditorPollContent(
            model = EditorPollComponent.Model(
                options = listOf(
                    PollChoiceOption(id = "1", text = "Some text here"),
                    PollChoiceOption(id = "2", text = "Another text here"),
                    PollChoiceOption(id = "3", text = ""),
                ),
                multiselectEnabled = false,
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
