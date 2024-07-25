package com.sedsoftware.tackle.compose.ui.editor.header.content

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_status_visibility_public
import tackle.shared.compose.generated.resources.editor_status_visibility_public_desc
import tackle.shared.compose.generated.resources.visibility_public

@Composable
internal fun VisibilitySelectorItem(
    iconRes: DrawableResource,
    textRes: StringResource,
    descriptionRes: StringResource,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .wrapContentWidth()
            .clickable(onClick = onClick)
    ) {

        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(size = 24.dp)
        )

        Column(
            modifier = modifier
                .weight(weight = 1f, false)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(textRes),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
            )

            Text(
                text = stringResource(descriptionRes),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview
@Composable
private fun VisibilitySelectorItemPreviewLight() {
    TackleScreenPreview {
        VisibilitySelectorItem(
            iconRes = Res.drawable.visibility_public,
            textRes = Res.string.editor_status_visibility_public,
            descriptionRes = Res.string.editor_status_visibility_public_desc,
        )
    }
}

@Preview
@Composable
private fun VisibilitySelectorItemPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        VisibilitySelectorItem(
            iconRes = Res.drawable.visibility_public,
            textRes = Res.string.editor_status_visibility_public,
            descriptionRes = Res.string.editor_status_visibility_public_desc,
        )
    }
}
