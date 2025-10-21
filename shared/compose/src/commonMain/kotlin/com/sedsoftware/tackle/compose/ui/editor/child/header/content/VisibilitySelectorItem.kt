package com.sedsoftware.tackle.compose.ui.editor.child.header.content

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
import org.jetbrains.compose.ui.tooling.preview.Preview
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.editor_status_visibility_direct
import tackle.shared.compose.generated.resources.editor_status_visibility_direct_desc
import tackle.shared.compose.generated.resources.editor_status_visibility_private
import tackle.shared.compose.generated.resources.editor_status_visibility_private_desc
import tackle.shared.compose.generated.resources.editor_status_visibility_public
import tackle.shared.compose.generated.resources.editor_status_visibility_public_desc
import tackle.shared.compose.generated.resources.editor_status_visibility_unlisted
import tackle.shared.compose.generated.resources.editor_status_visibility_unlisted_desc
import tackle.shared.compose.generated.resources.visibility_direct
import tackle.shared.compose.generated.resources.visibility_private
import tackle.shared.compose.generated.resources.visibility_public
import tackle.shared.compose.generated.resources.visibility_unlisted

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
            modifier = Modifier
                .weight(weight = 1f, false)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(resource = textRes),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
            )

            Text(
                text = stringResource(resource = descriptionRes),
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
        VisibilitySelectorItemPreviewContent()
    }
}

@Preview
@Composable
private fun VisibilitySelectorItemPreviewDark() {
    TackleScreenPreview(darkTheme = true) {
        VisibilitySelectorItemPreviewContent()
    }
}

@Composable
private fun VisibilitySelectorItemPreviewContent() {
    Column {
        VisibilitySelectorItem(
            iconRes = Res.drawable.visibility_public,
            textRes = Res.string.editor_status_visibility_public,
            descriptionRes = Res.string.editor_status_visibility_public_desc,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        VisibilitySelectorItem(
            iconRes = Res.drawable.visibility_direct,
            textRes = Res.string.editor_status_visibility_direct,
            descriptionRes = Res.string.editor_status_visibility_direct_desc,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        VisibilitySelectorItem(
            iconRes = Res.drawable.visibility_unlisted,
            textRes = Res.string.editor_status_visibility_unlisted,
            descriptionRes = Res.string.editor_status_visibility_unlisted_desc,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        VisibilitySelectorItem(
            iconRes = Res.drawable.visibility_private,
            textRes = Res.string.editor_status_visibility_private,
            descriptionRes = Res.string.editor_status_visibility_private_desc,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}
