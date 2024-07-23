package com.sedsoftware.tackle.compose.ui.editor.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_attachment
import tackle.shared.compose.generated.resources.editor_content_description_attach

@Composable
internal fun EditorToolbarButton(
    iconResource: DrawableResource,
    contentDescriptionResource: StringResource,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Box(modifier = modifier.alpha(alpha = if (isEnabled) 1f else 0.5f)) {
        IconButton(
            onClick = { if (isEnabled) onClick.invoke() },
            modifier = modifier
                .clip(shape = CircleShape)
                .background(
                    color = if (isActive)
                        MaterialTheme.colorScheme.secondaryContainer
                    else
                        MaterialTheme.colorScheme.surface
                ),
        ) {
            Icon(
                painter = painterResource(resource = iconResource),
                contentDescription = stringResource(resource = contentDescriptionResource),
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(all = 8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun EditorToolbarButtonPreviewNormal() {
    TackleScreenPreview {
        EditorToolbarButton(
            iconResource = Res.drawable.editor_attachment,
            contentDescriptionResource = Res.string.editor_content_description_attach,
            isActive = false,
            modifier = Modifier.size(size = 64.dp),
        )
    }
}

@Preview
@Composable
private fun EditorToolbarButtonPreviewDisabled() {
    TackleScreenPreview {
        EditorToolbarButton(
            iconResource = Res.drawable.editor_attachment,
            contentDescriptionResource = Res.string.editor_content_description_attach,
            isActive = false,
            isEnabled = false,
            modifier = Modifier.size(size = 64.dp),
        )
    }
}

@Preview
@Composable
private fun EditorToolbarButtonPreviewActive() {
    TackleScreenPreview {
        EditorToolbarButton(
            iconResource = Res.drawable.editor_attachment,
            contentDescriptionResource = Res.string.editor_content_description_attach,
            isActive = true,
            modifier = Modifier.size(size = 64.dp),
        )
    }
}
