package com.sedsoftware.tackle.compose.widget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview

@Composable
internal fun TackleButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(
            alpha = 0.5f,
        ),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(
            alpha = 0.75f,
        ),
    ),
) {
    Button(
        enabled = enabled,
        shape = shape,
        onClick = onClick,
        colors = colors,
        modifier = modifier
    ) {
        Text(
            text = text,
        )
    }
}

@Preview
@Composable
private fun TackleButtonPreviewLight() {
    TackleScreenPreview {
        TackleButtonPreviewContent()
    }
}

@Preview
@Composable
private fun TackleButtonPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        TackleButtonPreviewContent()
    }
}

@Composable
private fun TackleButtonPreviewContent() {
    Row {
        TackleButton(
            text = "Enabled",
            enabled = true,
            modifier = Modifier.padding(all = 16.dp),
        )

        TackleButton(
            text = "Disabled",
            enabled = false,
            modifier = Modifier.padding(all = 16.dp),
        )
    }
}
