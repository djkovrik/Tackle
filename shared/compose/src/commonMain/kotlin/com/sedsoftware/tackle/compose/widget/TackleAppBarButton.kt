package com.sedsoftware.tackle.compose.widget

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TackleAppBarButton(
    iconRes: DrawableResource,
    contentDescriptionRes: StringResource,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(resource = iconRes),
            contentDescription = stringResource(resource = contentDescriptionRes),
            modifier = Modifier.size(size = 24.dp)
        )
    }
}
