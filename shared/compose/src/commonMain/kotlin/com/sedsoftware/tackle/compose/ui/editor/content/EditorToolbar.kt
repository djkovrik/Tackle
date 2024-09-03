package com.sedsoftware.tackle.compose.ui.editor.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.getContentDescription
import com.sedsoftware.tackle.compose.extension.getIcon
import com.sedsoftware.tackle.compose.model.EditorToolbarItem
import com.sedsoftware.tackle.compose.widget.TackleToolbarButton

@Composable
internal fun EditorToolbar(
    items: List<EditorToolbarItem>,
    modifier: Modifier = Modifier,
    onClick: (EditorToolbarItem.Type) -> Unit = {},
) {
    Row(modifier = modifier) {
        items.forEach { item ->
            Box(modifier = Modifier.padding(end = 4.dp)) {
                TackleToolbarButton(
                    iconResource = item.type.getIcon(),
                    contentDescriptionResource = item.type.getContentDescription(),
                    isActive = item.active,
                    isEnabled = item.enabled,
                    onClick = { onClick.invoke(item.type) },
                )
            }
        }
    }
}
