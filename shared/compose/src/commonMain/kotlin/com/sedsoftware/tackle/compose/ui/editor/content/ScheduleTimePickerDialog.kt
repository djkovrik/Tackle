package com.sedsoftware.tackle.compose.ui.editor.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.common_cancel
import tackle.shared.compose.generated.resources.common_ok

@Composable
internal fun ScheduleTimePickerDialog(
    timePickerState: TimePickerState,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        ScheduleTimePickerDialogContent(
            timePickerState = timePickerState,
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            onConfirmation = onConfirmation,
        )
    }
}

@Composable
private fun ScheduleTimePickerDialogContent(
    timePickerState: TimePickerState,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {
    Card(
        modifier = Modifier.padding(all = 16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = modifier
        ) {
            TimeInput(
                state = timePickerState,
                modifier = Modifier.padding(all = 16.dp)
            )

            Row {
                Text(
                    text = stringResource(resource = Res.string.common_cancel),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(end = 8.dp, bottom = 16.dp)
                        .clickable(onClick = onDismissRequest)
                )

                Text(
                    text = stringResource(resource = Res.string.common_ok),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 16.dp, bottom = 16.dp)
                        .clickable(onClick = onConfirmation)
                )
            }
        }
    }
}
