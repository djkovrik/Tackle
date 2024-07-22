package com.sedsoftware.tackle.compose.ui.editor.poll.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_poll_duration

@Composable
internal fun CustomPollDurationDialog(
    model: EditorPollComponent.Model,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (PollDuration) -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        CustomPollDurationDialogContent(
            availableDurations = model.availableDurations,
            selectedDuration = model.duration,
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
    onDismissRequest: () -> Unit = {},
    onConfirmation: (PollDuration) -> Unit = {},
) {
    Card(
        modifier = Modifier.padding(all = 16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = Modifier.width(width = 240.dp)) {
            Text(
                text = stringResource(Res.string.editor_poll_duration),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )

            LazyColumn(
                modifier = modifier
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
                            onDismissRequest.invoke()
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
            availableDurations = PollDuration.entries.filter { it != PollDuration.NOT_SELECTED },
            selectedDuration = PollDuration.THREE_DAYS,
        )
    }
}
