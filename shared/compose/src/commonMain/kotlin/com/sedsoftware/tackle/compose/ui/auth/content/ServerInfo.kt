package com.sedsoftware.tackle.compose.ui.auth.content

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sedsoftware.tackle.compose.theme.TackleScreenPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ServerInfo(
    name: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        modifier = modifier
    ) {

        Text(
            text = name,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        )

        Text(
            text = description,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewServerInfoLight() {
    TackleScreenPreview {
        ServerInfo(
            name = "Mastodon",
            description = "A general-purpose Mastodon server with a 500 character limit. All languages are welcome."
        )
    }
}

@Preview
@Composable
private fun PreviewServerInfoDark() {
    TackleScreenPreview(darkTheme = true) {
        ServerInfo(
            name = "Mastodon",
            description = "A general-purpose Mastodon server with a 500 character limit. All languages are welcome."
        )
    }
}
