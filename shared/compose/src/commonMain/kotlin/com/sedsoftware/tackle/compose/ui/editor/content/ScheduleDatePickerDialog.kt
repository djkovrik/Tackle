package com.sedsoftware.tackle.compose.ui.editor.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.common_cancel
import tackle.shared.compose.generated.resources.common_ok

@Composable
internal fun ScheduleDatePickerDialog(
    datePickerState: DatePickerState,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Text(
                text = stringResource(resource = Res.string.common_ok),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(start = 4.dp, end = 16.dp, bottom = 16.dp)
                    .clickable(onClick = onConfirmation)
            )
        },
        dismissButton = {
            Text(
                text = stringResource(resource = Res.string.common_cancel),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(end = 4.dp, bottom = 16.dp)
                    .clickable(onClick = onDismissRequest)
            )
        },
        modifier = modifier,
    ) {
        DatePicker(
            state = datePickerState,
        )
    }
}
