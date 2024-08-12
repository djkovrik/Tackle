package com.sedsoftware.tackle.compose.ui.editor.header.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sedsoftware.tackle.compose.extension.getDescription
import com.sedsoftware.tackle.compose.extension.getIcon
import com.sedsoftware.tackle.compose.extension.getTitle
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.domain.model.type.StatusVisibility
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.common_cancel

@Composable
internal fun VisibilitySelectorDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (StatusVisibility) -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        VisibilitySelectorDialogContent(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            onConfirmation = onConfirmation,
        )
    }
}

@Composable
private fun VisibilitySelectorDialogContent(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (StatusVisibility) -> Unit = {},
) {
    Card(
        modifier = Modifier.padding(all = 16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = modifier
        ) {
            LazyColumn(
                modifier = modifier
                    .wrapContentWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                StatusVisibility.entries.forEachIndexed { index, visibility ->
                    if (visibility != StatusVisibility.UNKNOWN) {
                        item(key = visibility.name) {
                            VisibilitySelectorItem(
                                iconRes = visibility.getIcon(),
                                textRes = visibility.getTitle(),
                                descriptionRes = visibility.getDescription(),
                                onClick = { onConfirmation.invoke(visibility) }
                            )

                            if (index != StatusVisibility.entries.lastIndex - 1) {
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(start = 32.dp),
                                    color = MaterialTheme.colorScheme.secondary.copy(
                                        alpha = 0.5f,
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = stringResource(resource = Res.string.common_cancel),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .clickable(onClick = onDismissRequest)
            )
        }
    }
}

@Preview
@Composable
private fun VisibilitySelectorDialogContentPreviewLight() {
    TackleScreenPreview {
        VisibilitySelectorDialogContent()
    }
}

@Preview
@Composable
private fun VisibilitySelectorDialogContentPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        VisibilitySelectorDialogContent()
    }
}
