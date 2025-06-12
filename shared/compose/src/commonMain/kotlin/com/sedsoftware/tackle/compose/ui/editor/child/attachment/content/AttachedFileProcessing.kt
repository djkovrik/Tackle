package com.sedsoftware.tackle.compose.ui.editor.child.attachment.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_attachment_processing

@Composable
internal fun AttachedFileProcessing(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(resource = Res.string.editor_attachment_processing),
        color = MaterialTheme.colorScheme.onSecondary,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = MaterialTheme.shapes.extraSmall,
            )
            .padding(all = 4.dp)
    )
}

@Preview
@Composable
private fun AttachedFileProcessingPreviewLight() {
    TackleScreenPreview {
        AttachedFileProcessing()
    }
}

@Preview
@Composable
private fun AttachedFileProcessingPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        AttachedFileProcessing()
    }
}
