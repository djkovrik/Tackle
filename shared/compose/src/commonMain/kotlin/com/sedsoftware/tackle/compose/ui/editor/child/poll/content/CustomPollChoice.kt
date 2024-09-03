package com.sedsoftware.tackle.compose.ui.editor.child.poll.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.widget.TackleTextField
import com.sedsoftware.tackle.editor.poll.model.PollChoiceOption
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_close
import tackle.shared.compose.generated.resources.editor_poll_option

@Composable
internal fun CustomPollChoice(
    index: Int,
    option: PollChoiceOption,
    modifier: Modifier = Modifier,
    deletionAvailable: Boolean = false,
    onTextInput: (String, String) -> Unit = { _, _ -> },
    onDelete: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(bottom = 4.dp),
    ) {
        TackleTextField(
            value = option.text,
            onValueChange = { onTextInput.invoke(option.id, it) },
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            maxLines = 1,
            placeholder = {
                Text(
                    text = "${stringResource(resource = Res.string.editor_poll_option)} ${index + 1}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f,
                    ),
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
            ),
            modifier = Modifier
                .weight(weight = 1f, true)
                .height(height = 42.dp),
        )

        AnimatedVisibility(
            visible = deletionAvailable,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
        ) {
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(size = 32.dp)
            ) {
                Icon(
                    painterResource(resource = Res.drawable.editor_close),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(size = 16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun CustomPollChoicePreview() {
    TackleScreenPreview {
        Column {
            Spacer(modifier = Modifier.height(height = 8.dp))
            CustomPollChoice(
                index = 1,
                option = PollChoiceOption(id = "1", text = ""),
            )

            CustomPollChoice(
                index = 2,
                option = PollChoiceOption(id = "2", text = "Some text here"),
                deletionAvailable = true,
            )
        }
    }
}
