package com.sedsoftware.tackle.compose.ui.editor.child.poll.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sedsoftware.tackle.compose.extension.getTitle
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.editor.poll.EditorPollComponent
import com.sedsoftware.tackle.editor.poll.model.PollDuration
import com.sedsoftware.tackle.editor.poll.model.PollDuration.NOT_SELECTED
import com.sedsoftware.tackle.editor.poll.model.PollDuration.THREE_DAYS
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_poll_duration

@Composable
internal fun CustomPollDurationDialog(
    model: EditorPollComponent.Model,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onConfirmation: (PollDuration) -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        CustomPollDurationDialogContent(
            availableDurations = model.availableDurations,
            selectedDuration = model.duration,
            onDismiss = onDismiss,
            onConfirmation = onConfirmation,
            modifier = modifier,
        )
    }
}

@Composable
private fun CustomPollDurationDialogContent(
    availableDurations: List<PollDuration>,
    selectedDuration: PollDuration,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onConfirmation: (PollDuration) -> Unit = {},
) {
    Card(
        modifier = modifier.padding(all = 16.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Column(modifier = Modifier.width(width = 260.dp)) {
            Text(
                text = stringResource(resource = Res.string.editor_poll_duration),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(
                    items = availableDurations,
                    key = { it.seconds }
                ) { item ->
                    CustomPollDurationItem(
                        title = stringResource(resource = item.getTitle()),
                        checked = item == selectedDuration,
                        onClick = {
                            onConfirmation.invoke(item)
                            onDismiss.invoke()
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CustomPollDurationDialogContentPreview() {
    TackleScreenPreview {
        CustomPollDurationDialogContent(
            availableDurations = PollDuration.entries.filter { it != NOT_SELECTED },
            selectedDuration = THREE_DAYS,
        )
    }
}
