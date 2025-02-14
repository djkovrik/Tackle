package com.sedsoftware.tackle.compose.ui.editor.child.warning

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.widget.TackleTextField
import com.sedsoftware.tackle.compose.widget.TackleWarningContainer
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_close
import tackle.shared.compose.generated.resources.editor_warning_hint

@Composable
internal fun EditorWarningContent(
    text: String,
    modifier: Modifier = Modifier,
    onTextInput: (String) -> Unit = {},
    onTextClear: () -> Unit = {},
) {

    TackleWarningContainer(modifier = modifier,) {
        TackleTextField(
            value = text,
            onValueChange = onTextInput,
            singleLine = false,
            maxLines = 3,
            trailingIcon = {
                AnimatedVisibility(
                    visible = text.isNotBlank(),
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                ) {
                    Icon(
                        painterResource(resource = Res.drawable.editor_close),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .size(size = 18.dp)
                            .clickable(onClick = onTextClear),
                    )
                }
            },
            placeholder = {
                Text(
                    text = stringResource(resource = Res.string.editor_warning_hint),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f,
                    ),
                )
            },
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.extraSmall),
        )
    }
}

@Preview
@Composable
private fun EditorWarningContentPreviewLight() {
    TackleScreenPreview {
        EditorWarningContentPreviewContent()
    }
}


@Preview
@Composable
private fun EditorWarningContentPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        EditorWarningContentPreviewContent()
    }
}

@Composable
private fun EditorWarningContentPreviewContent() {
    Column(modifier = Modifier.padding(all = 16.dp)) {
        EditorWarningContent(
            text = "",
            modifier = Modifier.padding(all = 2.dp)
        )

        Spacer(modifier = Modifier.height(height = 16.dp))

        EditorWarningContent(
            text = "Warning text",
            modifier = Modifier.padding(all = 2.dp)
        )

        Spacer(modifier = Modifier.height(height = 16.dp))

        EditorWarningContent(
            text = "Warning text and more text\nSecond line\nThird line",
            modifier = Modifier.padding(all = 2.dp)
        )
    }
}
