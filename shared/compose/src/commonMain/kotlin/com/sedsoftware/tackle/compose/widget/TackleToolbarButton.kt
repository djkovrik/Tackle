package com.sedsoftware.tackle.compose.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.model.EditorToolbarItem
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import com.sedsoftware.tackle.compose.ui.editor.content.EditorToolbar
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun TackleToolbarButton(
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
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surface
                ),
        ) {
            Icon(
                painter = painterResource(resource = iconResource),
                contentDescription = stringResource(resource = contentDescriptionResource),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(all = 8.dp),
            )
        }
    }
}


@Preview
@Composable
private fun TackleToolbarButtonPreviewLight() {
    TackleScreenPreview {
        TackleToolbarButtonPreviewContent()
    }
}

@Preview
@Composable
private fun TackleToolbarButtonPreviewDark() {
    TackleScreenPreview {
        TackleToolbarButtonPreviewContent()
    }
}

@Composable
private fun TackleToolbarButtonPreviewContent() {
    Column {
        EditorToolbar(
            items = listOf(
                EditorToolbarItem(type = EditorToolbarItem.Type.ATTACH, active = false, enabled = true),
                EditorToolbarItem(type = EditorToolbarItem.Type.POLL, active = false, enabled = true),
                EditorToolbarItem(type = EditorToolbarItem.Type.EMOJIS, active = false, enabled = true),
                EditorToolbarItem(type = EditorToolbarItem.Type.WARNING, active = false, enabled = true),
                EditorToolbarItem(type = EditorToolbarItem.Type.SCHEDULE, active = true, enabled = true),
            )
        )
    }
}
