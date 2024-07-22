package com.sedsoftware.tackle.compose.ui.editor.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.extension.getContentDescription
import com.sedsoftware.tackle.compose.model.EditorToolbarItem
import com.sedsoftware.tackle.compose.extension.getIcon

@Composable
internal fun EditorToolbar(
    items: List<EditorToolbarItem>,
    modifier: Modifier = Modifier,
    onClick: (EditorToolbarItem.Type) -> Unit = {},
) {
    Row(modifier = modifier) {
        items.forEach { item ->
            Box(modifier = Modifier.padding(all = 4.dp)) {
                EditorToolbarButton(
                    iconResource = item.type.getIcon(),
                    contentDescriptionResource = item.type.getContentDescription(),
                    isActive = item.active,
                    isEnabled = item.enabled,
                    onClick = { onClick.invoke(item.type) },
                    modifier = Modifier.size(size = 48.dp),
                )
            }
        }
    }
}
